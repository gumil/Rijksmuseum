package io.gumil.rijksmuseum.list

import io.gumil.kaskade.DeferredValue
import io.gumil.kaskade.StateMachine
import io.gumil.kaskade.rx.toDeferred
import io.gumil.rijksmuseum.common.BaseViewModel
import io.gumil.rijksmuseum.data.repository.RijksRepository
import io.gumil.rijksmuseum.data.util.applySchedulers
import io.gumil.rijksmuseum.mapToItem
import io.reactivex.Observable
import timber.log.Timber

internal class RijksListViewModel(
        private val repository: RijksRepository
) : BaseViewModel<ListState, ListAction, ListResult>() {

    private var currentPage = 1

    override val stateMachine: StateMachine<ListState, ListAction, ListResult> =
            StateMachine<ListState, ListAction, ListResult>(ListState.Initial()).apply {
                addActionHandlers()
            }

    private fun StateMachine<ListState, ListAction, ListResult>.addActionHandlers() {
        addActionHandler(ListAction.Refresh::class) {
            val startWith = if (it.initialList.isNotEmpty()) {
                ListResult.Success(
                        it.initialList,
                        ListResult.Mode.REFRESH
                )
            } else {
                ListResult.InProgress(ListResult.Mode.REFRESH)
            }

            currentPage = 1
            repository.loadCollections(currentPage, ListResult.Mode.REFRESH, startWith).toDeferred()
        }

        addActionHandler(ListAction.Load::class) {
            repository.loadCollections(++currentPage, ListResult.Mode.LOAD_MORE).toDeferred()
        }

        addActionHandler(ListAction.OnItemClick::class) {
            DeferredValue(ListResult.GoTo(it.item))
        }
    }

    private fun RijksRepository.loadCollections(
            page: Int,
            mode: ListResult.Mode,
            startWith: ListResult = ListResult.InProgress(mode)
    ): Observable<ListResult> {
        return getCollections(page)
                .map {
                    ListResult.Success(it.map { it.mapToItem() }, mode)
                }
                .ofType(ListResult::class.java)
                .onErrorReturn {
                    Timber.e(it, "Error loading collections")
                    ListResult.Error()
                }
                .startWith(startWith)
                .applySchedulers()
    }
}