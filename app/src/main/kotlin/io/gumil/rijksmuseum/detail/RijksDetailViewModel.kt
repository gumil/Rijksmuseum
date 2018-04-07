package io.gumil.rijksmuseum.detail

import io.gumil.kaskade.DeferredValue
import io.gumil.kaskade.StateMachine
import io.gumil.rijksmuseum.CollectionDetailItem
import io.gumil.rijksmuseum.common.BaseViewModel
import io.gumil.rijksmuseum.data.repository.RijksRepository

internal class RijksDetailViewModel(
        private val repository: RijksRepository
) : BaseViewModel<DetailState, DetailAction, DetailResult>() {

    override val stateMachine: StateMachine<DetailState, DetailAction, DetailResult> =
            StateMachine<DetailState, DetailAction, DetailResult>(DetailState.View()).apply {
                addActionHandler(DetailAction.LoadFromItem::class) {
                    DeferredValue(DetailResult.Success(
                            it.collectionItem?.let {
                                CollectionDetailItem(it.title, it.image)
                            }
                    ))
                }
            }
}