package io.gumil.rijksmuseum.detail

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import io.gumil.rijksmuseum.CollectionItem
import io.gumil.rijksmuseum.R
import io.gumil.rijksmuseum.common.BaseFragment
import io.gumil.rijksmuseum.common.BaseViewModel
import io.gumil.rijksmuseum.common.load
import io.gumil.rijksmuseum.common.preLoad
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_detail.*
import javax.inject.Inject

internal class RijksDetailFragment : BaseFragment<DetailState, DetailAction>() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override val layoutId: Int = R.layout.fragment_detail

    override val viewModel: BaseViewModel<DetailState, DetailAction, *>
        get() = ViewModelProviders.of(this,
                viewModelFactory)[RijksDetailViewModel::class.java]

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

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

    override fun actions(): Observable<DetailAction> {
        return getLoadAction()
    }

    override fun DetailState.render() = when (this) {
        is DetailState.View -> {
            detailItem?.let {
                if (it.height > -1 && it.width > -1) {
                    compositeDisposable.add(artImage.preLoad(it.image))
                } else {
                    artImage.load(it.image) { centerCrop() }
                }
            }
            Unit //Kotlin warns return type
        }
        is DetailState.Error -> showSnackbarError(message)
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