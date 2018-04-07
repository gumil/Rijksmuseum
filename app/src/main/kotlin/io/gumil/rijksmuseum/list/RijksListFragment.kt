package io.gumil.rijksmuseum.list

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.jakewharton.rxbinding2.support.v4.widget.refreshes
import io.gumil.rijksmuseum.R
import io.gumil.rijksmuseum.common.*
import io.reactivex.Observable
import kotlinx.android.synthetic.main.fragment_list.*
import javax.inject.Inject

internal class RijksListFragment : BaseFragment<ListState, ListAction>() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override val layoutId: Int = R.layout.fragment_list

    override val viewModel: BaseViewModel<ListState, ListAction, *>
        get() = ViewModelProviders.of(this,
                viewModelFactory)[RijksListViewModel::class.java]

    private val rijksViewItem = RijksViewItem()
    private val adapter = ItemAdapter(rijksViewItem).apply {
        footerItem = FooterItem(R.layout.item_progress)
    }

    override fun initializeViews(view: View) {
        title = getString(R.string.app_name)
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
        is ListState.GoToDetail -> TODO()
    }

    companion object {
        fun newInstance(): RijksListFragment = RijksListFragment()
    }
}