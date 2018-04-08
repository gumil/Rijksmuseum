package io.gumil.rijksmuseum.list

import io.gumil.kaskade.Action
import io.gumil.kaskade.Result
import io.gumil.kaskade.State
import io.gumil.rijksmuseum.CollectionItem
import io.gumil.rijksmuseum.R
import io.gumil.rijksmuseum.data.response.LinkType

internal sealed class ListState : State {

    data class Initial(
            val collections: List<CollectionItem> = emptyList(),
            val isLoading: Boolean = true
    ) : ListState()

    data class LoadMore(
            val collections: List<CollectionItem> = emptyList(),
            val isLoading: Boolean = true
    ) : ListState()

    data class Error(
            val errorMessage: Int
    ) : ListState()

    data class GoToDetail(
            val item: CollectionItem
    ) : ListState()
}

internal sealed class ListAction : Action {
    data class Refresh(
            val initialList: List<CollectionItem> = emptyList(),
            val param: Pair<LinkType, String>? = null
    ) : ListAction()

    data class Load(
            val param: Pair<LinkType, String>? = null
    ): ListAction()

    data class OnItemClick(
            val item: CollectionItem
    ) : ListAction()
}

internal sealed class ListResult : Result<ListState> {

    data class Success(
            private val collections: List<CollectionItem> = emptyList(),
            private val mode: Mode
    ) : ListResult() {
        override fun reduceToState(oldState: ListState): ListState =
                mode.renderListLoading(collections, false)
    }

    class Error : ListResult() {
        override fun reduceToState(oldState: ListState): ListState =
                ListState.Error(R.string.error_loading)
    }

    data class InProgress(
            private val mode: Mode
    ) : ListResult() {
        override fun reduceToState(oldState: ListState): ListState =
                mode.renderListLoading(emptyList(), true)
    }

    data class GoTo(
            private val item: CollectionItem
    ) : ListResult() {
        override fun reduceToState(oldState: ListState): ListState =
                ListState.GoToDetail(item)
    }

    enum class Mode {
        REFRESH, LOAD_MORE
    }

    fun ListResult.Mode.renderListLoading(
            list: List<CollectionItem>,
            isLoading: Boolean
    ): ListState = when (this) {
        ListResult.Mode.REFRESH -> ListState.Initial(list, isLoading)
        ListResult.Mode.LOAD_MORE -> ListState.LoadMore(list, isLoading)
    }
}