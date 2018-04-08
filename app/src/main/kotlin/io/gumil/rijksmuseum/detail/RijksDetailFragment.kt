package io.gumil.rijksmuseum.detail

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.TextView
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import io.gumil.rijksmuseum.CollectionDetailItem
import io.gumil.rijksmuseum.CollectionItem
import io.gumil.rijksmuseum.R
import io.gumil.rijksmuseum.common.BaseFragment
import io.gumil.rijksmuseum.common.BaseViewModel
import io.gumil.rijksmuseum.common.load
import io.gumil.rijksmuseum.common.preLoad
import io.gumil.rijksmuseum.data.response.LinkType
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_detail.*
import javax.inject.Inject

internal class RijksDetailFragment : BaseFragment<DetailState, DetailAction>() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override val layoutId: Int = R.layout.fragment_detail

    override val viewModel: BaseViewModel<DetailState, DetailAction, *>
        get() = ViewModelProviders.of(this,
                viewModelFactory)[RijksDetailViewModel::class.java]

    private val compositeDisposable = CompositeDisposable()

    private val spanOnClickSubject = PublishSubject.create<Pair<String, LinkType>>()

    private val bottomSheetBehavior by lazy {
        BottomSheetBehavior.from(bottomSheet)
    }

    override fun initializeViews(view: View) {
        super.initializeViews(view)
        swipeRefreshLayout.isEnabled = false
        detailsTags.movementMethod = LinkMovementMethod.getInstance()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        compositeDisposable.dispose()
    }

    private fun getLoadAction(): Observable<DetailAction> {
        return rxLifecycle.filter { it == Lifecycle.Event.ON_START }.map {
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
                        compositeDisposable.add(artImage.preLoad(it.image))
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
            is DetailState.Error -> showSnackbarError(message)
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

        val spannableBuilder = SpannableStringBuilder(getString(R.string.tags)).append(" ")

        spannableBuilder.addToBuilder(detailItem.maker, LinkType.MAKER)
        spannableBuilder.addToBuilder(detailItem.date, LinkType.PERIOD, detailItem.period.toString())
        spannableBuilder.addAllToBuilder(detailItem.types, LinkType.TYPE)
        spannableBuilder.addAllToBuilder(detailItem.materials, LinkType.MATERIAL)
        spannableBuilder.addAllToBuilder(detailItem.techniques, LinkType.TECHNIQUE)

        if (spannableBuilder.trim().isNotBlank()) {
            detailsTags.setTextAndVisibility(spannableBuilder)
        }
    }

    private fun SpannableStringBuilder.addAllToBuilder(strings: List<String>?, type: LinkType) {
        strings?.let {
            if (it.isNotEmpty()) {
                it.forEach {
                    addToBuilder(it, type)
                }
            }
        }
    }

    private fun SpannableStringBuilder.addToBuilder(string: String?, type: LinkType, customString: String? = null) {
        string?.let {
            append(it.toClickableSpan(type, customString))
                    .append(" ")
        }
    }

    private fun String.toClickableSpan(type: LinkType, customString: String? = null) : Spannable {
        return SpannableString(this).apply {
            setSpan(object : ClickableSpan() {
                override fun onClick(widget: View?) {
                    spanOnClickSubject.onNext((customString ?: this@toClickableSpan) to type)
                }
            }, 0, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
    }

    private fun TextView.setTextAndVisibility(text: CharSequence?) {
        text?.let {
            this.text = it
            visibility = View.VISIBLE
        } ?: let { visibility = View.GONE }
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