package io.gumil.rijksmuseum.detail

import io.gumil.kaskade.Action
import io.gumil.kaskade.Result
import io.gumil.kaskade.State
import io.gumil.rijksmuseum.CollectionDetailItem
import io.gumil.rijksmuseum.CollectionItem
import io.gumil.rijksmuseum.R

internal sealed class DetailState : State {
    data class View(
            val detailItem: CollectionDetailItem? = null
    ) : DetailState()

    data class Error(
            val message: Int
    ) : DetailState()
}

internal sealed class DetailAction : Action {
    data class LoadItem(
            val collectionItem: CollectionItem?
    ) : DetailAction()
}

internal sealed class DetailResult : Result<DetailState> {

    data class Success(
            private val detailItem: CollectionDetailItem?
    ) : DetailResult() {
        override fun reduceToState(oldState: DetailState): DetailState =
                DetailState.View(detailItem)
    }

    class Error : DetailResult() {
        override fun reduceToState(oldState: DetailState): DetailState =
                DetailState.Error(R.string.error_loading_single)
    }
}