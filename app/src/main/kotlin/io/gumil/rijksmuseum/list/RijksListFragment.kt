package io.gumil.rijksmuseum.list

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.jakewharton.rxbinding2.support.v4.widget.refreshes
import io.gumil.rijksmuseum.R
import io.gumil.rijksmuseum.common.*
import io.gumil.rijksmuseum.data.response.LinkType
import io.gumil.rijksmuseum.detail.RijksDetailFragment
import io.gumil.rijksmuseum.main.Backstack
import io.reactivex.Observable
import kotlinx.android.synthetic.main.fragment_list.*
import javax.inject.Inject

internal class RijksListFragment : BaseFragment<ListState, ListAction>() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var backstack: Backstack

    override val layoutId: Int = R.layout.fragment_list

    override val viewModel: BaseViewModel<ListState, ListAction, *>
        get() = ViewModelProviders.of(this,
                viewModelFactory)[RijksListViewModel::class.java]

    private val rijksViewItem = RijksViewItem()
    private val adapter = ItemAdapter(rijksViewItem).apply {
        footerItem = FooterItem(R.layout.item_progress)
    }

    private val searchString by lazy {
        arguments?.getString(ARG_SEARCH)
    }

    private val type by lazy {
        arguments?.getInt(ARG_TYPE)?.let {
            LinkType.values()[it]
        }
    }

    override fun initializeViews(view: View) {
        setToolbar(toolbar)
        showBack(searchString != null)
        title = searchString ?: getString(R.string.app_name)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
    }

    private fun getLoadAction(): Observable<ListAction> {
        return rxLifecycle.filter { it == Lifecycle.Event.ON_START }.map {
            ListAction.Refresh(emptyList())
        }
    }

    override fun actions(): Observable<ListAction> = Observable.merge(
            getLoadAction(),
            adapter.prefetch().map {
                ListAction.Load
            },
            swipeRefreshLayout.refreshes().map {
                ListAction.Refresh(adapter.list)
            },
            rijksViewItem.itemClick().map {
                ListAction.OnItemClick(it)
            }
    )

    override fun ListState.render() = when (this) {
        is ListState.Initial -> {
            swipeRefreshLayout.isRefreshing = isLoading
            adapter.list = collections
        }
        is ListState.LoadMore -> {
            if (isLoading) {
                adapter.showFooter()
            } else {
                adapter.addItems(collections)
            }
        }
        is ListState.Error -> {
            showSnackbarError(errorMessage)
            swipeRefreshLayout.isRefreshing = false
        }
        is ListState.GoToDetail -> backstack.goTo(RijksDetailFragment.newInstance(item))
    }

    companion object {
        private const val ARG_SEARCH = "arg_search"
        private const val ARG_TYPE = "arg_type"

        fun newInstance(type: Int = -1, search: String = ""): RijksListFragment = RijksListFragment().apply {
            if (search.isNotBlank() && type > -1) {
                arguments = Bundle().apply {
                    putInt(ARG_TYPE, type)
                    putString(ARG_SEARCH, search)
                }
            }
        }
    }
}