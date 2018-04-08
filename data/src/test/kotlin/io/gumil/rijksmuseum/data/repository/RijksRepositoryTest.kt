package io.gumil.rijksmuseum.data.repository

import io.gumil.rijksmuseum.data.createMockResponse
import io.gumil.rijksmuseum.data.network.ApiFactory
import io.gumil.rijksmuseum.data.readFromFile
import io.gumil.rijksmuseum.data.response.*
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.observers.TestObserver
import io.reactivex.schedulers.Schedulers
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

internal class RijksRepositoryTest {

    private val mockServer = MockWebServer().apply {
        start()
    }

    private val rijksApi = ApiFactory.create(null, true,
            mockServer.url("/").toString())

    private val repository = DataRijksRepository(rijksApi)

    @Before
    fun setUp() {
        RxAndroidPlugins.setInitMainThreadSchedulerHandler {
            Schedulers.trampoline()
        }
    }

    @After
    fun tearDown() {
        RxAndroidPlugins.reset()
    }

    @Test
    fun testGetCollections() {
        val subscriber = TestObserver<List<ArtObject>>()
        mockServer.enqueue(createMockResponse(readFromFile("list_success.json")))
        repository.getCollections().subscribe(subscriber)

        subscriber.awaitTerminalEvent()
        subscriber.assertNoErrors()
        subscriber.assertNoTimeout()
        subscriber.assertValueCount(1)
        subscriber.assertComplete()
        assertEquals(3, subscriber.events.size)

        val list = subscriber.values().first()
        assertEquals(10, list.size)
        assertEquals("SK-A-2815", list.first().objectNumber)
        assertEquals("SK-C-149", list.last().objectNumber)
    }

    @Test
    fun testGetCollectionsEmpty() {
        val subscriber = TestObserver<List<ArtObject>>()
        mockServer.enqueue(createMockResponse(readFromFile("list_empty.json")))
        repository.getCollections().subscribe(subscriber)

        subscriber.awaitTerminalEvent()
        subscriber.assertNoErrors()
        subscriber.assertNoTimeout()
        subscriber.assertValueCount(1)
        subscriber.assertComplete()
        assertEquals(3, subscriber.events.size)

        val list = subscriber.values().first()
        assertTrue(list.isEmpty())
    }

    @Test
    fun testGetCollectionDetail() {
        val subscriber = TestObserver<ArtObjectDetail>()
        mockServer.enqueue(createMockResponse(readFromFile("detail_success.json")))
        repository.getCollection("SK-A-28152").subscribe(subscriber)

        subscriber.awaitTerminalEvent()
        subscriber.assertNoErrors()
        subscriber.assertNoTimeout()
        subscriber.assertValueCount(1)
        subscriber.assertComplete()
        assertEquals(3, subscriber.events.size)

        val expected = ArtObjectDetail(
                "en-SK-A-2815",
                "The Seven Works of Mercy",
                WebImage("http://lh3.googleusercontent.com/PKiHHjb5ov-6RQm4WjTmQL7gQsiqz-jTgy6aJoUfXFPB-FPJF2XMcgjdpf0rMfPtQoePma2oYkAiN83RDwlyU34phg=s0"),
                listOf("painting"),
                "Master of Alkmaar",
                listOf("panel", "oil paint (paint)"),
                emptyList(),
                Dating("1504", 16),
                "h 101cm × w 54cm",
                "Master of Alkmaar (active 1490–1510), Alkmaar, 1504, oil on panel",
                Label("A Dutch city is the backdrop to this narrative that shows how a good Christian should help those in need. Christ stands among the spectators in almost every panel. The scenes give an impression of urban society around 1500. The work was badly damaged during the Iconoclasm of 1566, when Roman Catholic churches were vandalized by Protestants.")
        )

        assertEquals(expected, subscriber.values().first())
    }

    @Test
    fun testGetCollectionDetailEmpty() {
        val subscriber = TestObserver<ArtObjectDetail>()
        mockServer.enqueue(createMockResponse(readFromFile("detail_empty.json")))
        repository.getCollection("SK-A-28152").subscribe(subscriber)

        subscriber.awaitTerminalEvent()
        subscriber.assertNoTimeout()
        subscriber.assertNotComplete()
        subscriber.assertError(ItemNotFoundException::class.java)
        assertEquals(3, subscriber.events.size)
    }
}