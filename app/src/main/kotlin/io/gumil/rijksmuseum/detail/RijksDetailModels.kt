package io.gumil.rijksmuseum.detail

import io.gumil.kaskade.Action
import io.gumil.kaskade.Result
import io.gumil.kaskade.State
import io.gumil.rijksmuseum.CollectionDetailItem
import io.gumil.rijksmuseum.CollectionItem
import io.gumil.rijksmuseum.R
import io.gumil.rijksmuseum.data.response.LinkType

internal sealed class DetailState : State {
    data class View(
            val detailItem: CollectionDetailItem? = null,
            val isLoaded: Boolean = false
    ) : DetailState()

    data class Error(
            val message: Int
    ) : DetailState()
}

internal sealed class DetailAction : Action {
    data class LoadItem(
            val collectionItem: CollectionItem?
    ) : DetailAction()

    data class OpenLink(
            val tag: String,
            val type: LinkType
    ) : DetailAction()
}

internal sealed class DetailResult : Result<DetailState> {

    data class Success(
            private val detailItem: CollectionDetailItem?,
            private val isLoaded: Boolean = false
    ) : DetailResult() {
        override fun reduceToState(oldState: DetailState): DetailState =
                DetailState.View(detailItem, isLoaded)
    }

    class Error : DetailResult() {
        override fun reduceToState(oldState: DetailState): DetailState =
                DetailState.Error(R.string.error_loading_single)
    }
}