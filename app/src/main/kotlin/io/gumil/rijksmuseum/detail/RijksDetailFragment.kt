package io.gumil.rijksmuseum.detail

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.text.SpannableStringBuilder
import android.text.method.LinkMovementMethod
import android.view.View
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import io.gumil.rijksmuseum.CollectionDetailItem
import io.gumil.rijksmuseum.CollectionItem
import io.gumil.rijksmuseum.R
import io.gumil.rijksmuseum.common.*
import io.gumil.rijksmuseum.data.response.LinkType
import io.gumil.rijksmuseum.list.RijksListFragment
import io.gumil.rijksmuseum.main.Backstack
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_detail.*
import javax.inject.Inject

internal class RijksDetailFragment : BaseFragment<DetailState, DetailAction>() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var backstack: Backstack

    override val layoutId: Int = R.layout.fragment_detail

    override val viewModel: BaseViewModel<DetailState, DetailAction, *>
        get() = ViewModelProviders.of(this,
                viewModelFactory)[RijksDetailViewModel::class.java]

    private val compositeDisposable = CompositeDisposable()

    private val spanOnClickSubject = PublishSubject.create<Pair<LinkType, String>>()

    private val bottomSheetBehavior by lazy {
        BottomSheetBehavior.from(bottomSheet)
    }

    override fun initializeViews(view: View) {
        super.initializeViews(view)
        swipeRefreshLayout.isEnabled = false
        detailsTags.movementMethod = LinkMovementMethod.getInstance()
        artImage.setZoomable(false)

        bottomSheet.setOnClickListener {
            if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_COLLAPSED) {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        compositeDisposable.dispose()
        spanOnClickSubject.unsubscribeOn(AndroidSchedulers.mainThread())
    }

    private fun getLoadAction(): Observable<DetailAction> {
        return onViewCreatedObservable.map {
            arguments?.getParcelable<CollectionItem>(ARG_ITEM)?.let {
                DetailAction.LoadItem(it)
            } ?: DetailAction.LoadItem(null)
        }
    }

    override fun actions(): Observable<DetailAction> = Observable.merge(
            getLoadAction(),
            spanOnClickSubject.map {
                DetailAction.OpenLink(it.first, it.second)
            }
    )

    override fun DetailState.render() {
        when (this) {
            is DetailState.View -> {
                detailItem?.let {
                    if (isLoaded) {
                        swipeRefreshLayout.isRefreshing = false
                        swipeRefreshLayout.isEnabled = false
                        compositeDisposable.add(artImage.preLoad(it.image) {
                            artImage.setZoomable(true)
                        })
                    } else {
                        swipeRefreshLayout.isEnabled = true
                        swipeRefreshLayout.isRefreshing = true
                        artImage.load(it.image, {
                            placeholder(R.drawable.ic_image_24dp)
                        }, DrawableTransitionOptions.withCrossFade())
                    }

                    populateDetails(it)
                }
            }
            is DetailState.GoTo -> backstack.goTo(RijksListFragment.newInstance(type.ordinal, tag))
            is DetailState.Error -> {
                swipeRefreshLayout.isRefreshing = false
                swipeRefreshLayout.isEnabled = false
                showSnackbarError(message)
            }
        }
    }

    private fun populateDetails(detailItem: CollectionDetailItem) {
        detailsTitle.text = detailItem.title
        detailsTitle.post {
            bottomSheetBehavior.peekHeight = detailsTitle.height
        }

        detailsMakerLine.setTextAndVisibility(detailItem.makerLine)
        detailsSubtitle.setTextAndVisibility(detailItem.subtitle)
        detailsDescription.setTextAndVisibility(detailItem.description)

        val spannableBuilder = SpannableStringBuilder(getString(R.string.tags))
                .append(" ")
                .addToBuilder(detailItem.maker, LinkType.MAKER)
                .addToBuilder(detailItem.date, LinkType.PERIOD, detailItem.period.toString())
                .addAllToBuilder(detailItem.types, LinkType.TYPE)
                .addAllToBuilder(detailItem.materials, LinkType.MATERIAL)
                .addAllToBuilder(detailItem.techniques, LinkType.TECHNIQUE)

        if (spannableBuilder.trim().isNotBlank()) {
            detailsTags.setTextAndVisibility(spannableBuilder)
        }
    }

    private fun SpannableStringBuilder.addAllToBuilder(
            strings: List<String>?,
            type: LinkType
    ): SpannableStringBuilder {
        strings?.let {
            if (it.isNotEmpty()) {
                it.forEach {
                    addToBuilder(it, type)
                }
            }
        }
        return this
    }

    private fun SpannableStringBuilder.addToBuilder(
            string: String?, type: LinkType,
            customString: String? = null
    ): SpannableStringBuilder {
        string?.let {
            append(it.toClickableSpan {
                spanOnClickSubject.onNext(type to (customString ?: it))
            }).append(" ")
        }

        return this
    }

    companion object {
        private const val ARG_ITEM = "arg_item"

        fun newInstance(collectionItem: CollectionItem): RijksDetailFragment =
                RijksDetailFragment().apply {
                    arguments = Bundle().apply {
                        putParcelable(ARG_ITEM, collectionItem)
                    }
                }
    }
}