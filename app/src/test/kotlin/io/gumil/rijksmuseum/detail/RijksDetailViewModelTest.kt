package io.gumil.rijksmuseum.detail

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.Observer
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.verifyNoMoreInteractions
import io.gumil.rijksmuseum.TestRijksRepository
import io.gumil.rijksmuseum.CollectionDetailItem
import io.gumil.rijksmuseum.CollectionItem
import io.gumil.rijksmuseum.R
import io.gumil.rijksmuseum.TrampolineSchedulerRule
import io.gumil.rijksmuseum.data.response.LinkType
import io.gumil.rijksmuseum.data.util.just
import org.junit.Rule
import org.junit.Test

internal class RijksDetailViewModelTest {

    @get:Rule
    val instantTaskRule = InstantTaskExecutorRule()

    @get:Rule
    val trampolineRule = TrampolineSchedulerRule()

    private val viewModel = RijksDetailViewModel(TestRijksRepository())

    private val detailItem = CollectionDetailItem(
            "The Seven Works of Mercy",
            "http://lh3.googleusercontent.com/PKiHHjb5ov-6RQm4WjTmQL7gQsiqz-jTgy6aJoUfXFPB-FPJF2XMcgjdpf0rMfPtQoePma2oYkAiN83RDwlyU34phg=s0",
            "Master of Alkmaar (active 1490–1510), Alkmaar, 1504, oil on panel",
            "h 101cm × w 54cm",
            "A Dutch city is the backdrop to this narrative that shows how a good Christian should help those in need. Christ stands among the spectators in almost every panel. The scenes give an impression of urban society around 1500. The work was badly damaged during the Iconoclasm of 1566, when Roman Catholic churches were vandalized by Protestants.",
            listOf("painting"),
            "Master of Alkmaar",
            "1504",
            16,
            emptyList(),
            listOf("panel", "oil paint (paint)")
    )

    private val item = CollectionItem(
            "SK-A-2815",
            "The Seven Works of Mercy",
            "https://lh3.googleusercontent.com/lEPMS16z9Y8aEsbUYI-zE-rTFqKa8AE63loPpzSkZWFFJ8RSxA7BKvgGsn3yOvC6udiX61dF4_smw3SG0-yfksfQ8YDs",
            "https://lh3.googleusercontent.com/PKiHHjb5ov-6RQm4WjTmQL7gQsiqz-jTgy6aJoUfXFPB-FPJF2XMcgjdpf0rMfPtQoePma2oYkAiN83RDwlyU34phg"
    )

    @Test
    fun actionLoadItemWhenItemIsNull() {
        val observer = mock<Observer<DetailState>>()
        viewModel.state.observeForever(observer)

        viewModel.processActions(DetailAction.LoadItem(null).just())

        verify(observer).onChanged(DetailState.View())
        verifyNoMoreInteractions(observer)
    }

    @Test
    fun actionLoadItem() {
        val observer = mock<Observer<DetailState>>()
        viewModel.state.observeForever(observer)

        viewModel.processActions(DetailAction.LoadItem(item).just())

        verify(observer).onChanged(DetailState.View(CollectionDetailItem("The Seven Works of Mercy", "https://lh3.googleusercontent.com/PKiHHjb5ov-6RQm4WjTmQL7gQsiqz-jTgy6aJoUfXFPB-FPJF2XMcgjdpf0rMfPtQoePma2oYkAiN83RDwlyU34phg")))
        verify(observer).onChanged(DetailState.View(detailItem, true))
        verifyNoMoreInteractions(observer)
    }

    @Test
    fun actionLoadItemError() {
        val observer = mock<Observer<DetailState>>()
        viewModel.state.observeForever(observer)

        viewModel.processActions(DetailAction.LoadItem(CollectionItem("", "", "", "")).just())

        verify(observer).onChanged(DetailState.View(CollectionDetailItem("", "")))
        verify(observer).onChanged(DetailState.Error(R.string.error_loading_single))
        verifyNoMoreInteractions(observer)
    }

    @Test
    fun actionOpenLink() {
        val observer = mock<Observer<DetailState>>()
        viewModel.state.observeForever(observer)

        viewModel.processActions(DetailAction.OpenLink(LinkType.TYPE, "type").just())

        verify(observer).onChanged(DetailState.GoTo(LinkType.TYPE, "type"))
        verifyNoMoreInteractions(observer)
    }
}