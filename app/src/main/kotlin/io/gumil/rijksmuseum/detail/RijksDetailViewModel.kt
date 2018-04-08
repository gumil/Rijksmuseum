package io.gumil.rijksmuseum.detail

import io.gumil.kaskade.DeferredValue
import io.gumil.kaskade.StateMachine
import io.gumil.kaskade.rx.toDeferred
import io.gumil.rijksmuseum.CollectionDetailItem
import io.gumil.rijksmuseum.CollectionItem
import io.gumil.rijksmuseum.common.BaseViewModel
import io.gumil.rijksmuseum.data.repository.RijksRepository
import io.gumil.rijksmuseum.data.util.applySchedulers
import io.gumil.rijksmuseum.mapToItem
import io.reactivex.Observable
import timber.log.Timber

internal class RijksDetailViewModel(
        private val repository: RijksRepository
) : BaseViewModel<DetailState, DetailAction, DetailResult>() {

    override val stateMachine: StateMachine<DetailState, DetailAction, DetailResult> =
            StateMachine<DetailState, DetailAction, DetailResult>(DetailState.View()).apply {
                addActionHandler(DetailAction.LoadItem::class) {
                    it.collectionItem?.let {
                        repository.loadItem(it).toDeferred()
                    } ?: DeferredValue<DetailResult>(
                            DetailResult.Success(null)
                    )
                }

                addActionHandler(DetailAction.OpenLink::class) {
                    DeferredValue(DetailResult.GoTo(it.type, it.tag))
                }
            }

    private fun RijksRepository.loadItem(item: CollectionItem): Observable<DetailResult> {
        return getCollection(item.id)
                .map {
                    DetailResult.Success(it.mapToItem(), true)
                }
                .ofType(DetailResult::class.java)
                .onErrorReturn {
                    Timber.e(it, "Error loading collections")
                    DetailResult.Error()
                }
                .startWith(DetailResult.Success(
                        CollectionDetailItem(item.title, item.webImage)
                ))
                .applySchedulers()
    }
}