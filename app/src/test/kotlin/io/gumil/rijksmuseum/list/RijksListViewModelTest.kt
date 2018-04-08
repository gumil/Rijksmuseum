package io.gumil.rijksmuseum.list

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.Observer
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.verifyNoMoreInteractions
import io.gumil.rijksmuseum.TestRijksRepository
import io.gumil.rijksmuseum.CollectionItem
import io.gumil.rijksmuseum.R
import io.gumil.rijksmuseum.TrampolineSchedulerRule
import io.gumil.rijksmuseum.data.response.LinkType
import io.gumil.rijksmuseum.data.util.just
import org.junit.Rule
import org.junit.Test

internal class RijksListViewModelTest {

    @get:Rule
    val instantTaskRule = InstantTaskExecutorRule()

    @get:Rule
    val trampolineRule = TrampolineSchedulerRule()

    private val viewModel = RijksListViewModel(TestRijksRepository())

    private val list = listOf(
            CollectionItem(
                    "SK-A-2815",
                    "The Seven Works of Mercy",
                    "https://lh3.googleusercontent.com/lEPMS16z9Y8aEsbUYI-zE-rTFqKa8AE63loPpzSkZWFFJ8RSxA7BKvgGsn3yOvC6udiX61dF4_smw3SG0-yfksfQ8YDs",
                    "https://lh3.googleusercontent.com/PKiHHjb5ov-6RQm4WjTmQL7gQsiqz-jTgy6aJoUfXFPB-FPJF2XMcgjdpf0rMfPtQoePma2oYkAiN83RDwlyU34phg"
            ),
            CollectionItem(
                    "number",
                    "title",
                    "headerurl",
                    "weburl"
            )
    )

    @Test
    fun actionRefresh() {
        val observer = mock<Observer<ListState>>()
        viewModel.state.observeForever(observer)

        viewModel.processActions(ListAction.Refresh(emptyList()).just())

        verify(observer).onChanged(ListState.Initial())
        verify(observer).onChanged(ListState.Initial(list, false))
        verifyNoMoreInteractions(observer)
    }

    @Test
    fun actionRefreshError() {
        val observer = mock<Observer<ListState>>()
        viewModel.state.observeForever(observer)

        viewModel.processActions(ListAction.Refresh(emptyList(), LinkType.PERIOD to "").just())

        verify(observer).onChanged(ListState.Initial())
        verify(observer).onChanged(ListState.Error(R.string.error_loading))
        verifyNoMoreInteractions(observer)
    }

    @Test
    fun actionLoad() {
        val observer = mock<Observer<ListState>>()
        viewModel.state.observeForever(observer)

        viewModel.processActions(ListAction.Load().just())

        verify(observer).onChanged(ListState.LoadMore())
        verify(observer).onChanged(ListState.LoadMore(list, false))
        verifyNoMoreInteractions(observer)
    }

    @Test
    fun actionOnItemClick() {
        val observer = mock<Observer<ListState>>()
        viewModel.state.observeForever(observer)

        val item = list[0]
        viewModel.processActions(ListAction.OnItemClick(item).just())

        verify(observer).onChanged(ListState.GoToDetail(item))
        verifyNoMoreInteractions(observer)
    }
}