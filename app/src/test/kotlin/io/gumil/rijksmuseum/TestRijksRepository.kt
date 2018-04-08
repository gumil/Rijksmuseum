package io.gumil.rijksmuseum

import io.gumil.rijksmuseum.data.repository.ItemNotFoundException
import io.gumil.rijksmuseum.data.repository.RijksRepository
import io.gumil.rijksmuseum.data.response.*
import io.gumil.rijksmuseum.data.util.error
import io.gumil.rijksmuseum.data.util.just
import io.reactivex.Observable

internal class TestRijksRepository : RijksRepository {

    override fun getCollections(page: Int, param: Pair<LinkType, String>?): Observable<List<ArtObject>> {
        return if (param?.first == LinkType.PERIOD) {
            RuntimeException().error()
        } else {
            listOf(
                    ArtObject(
                            "SK-A-2815",
                            "The Seven Works of Mercy",
                            WebImage("https://lh3.googleusercontent.com/PKiHHjb5ov-6RQm4WjTmQL7gQsiqz-jTgy6aJoUfXFPB-FPJF2XMcgjdpf0rMfPtQoePma2oYkAiN83RDwlyU34phg"),
                            HeaderImage("https://lh3.googleusercontent.com/lEPMS16z9Y8aEsbUYI-zE-rTFqKa8AE63loPpzSkZWFFJ8RSxA7BKvgGsn3yOvC6udiX61dF4_smw3SG0-yfksfQ8YDs")
                    ),
                    ArtObject(
                            "number",
                            "title",
                            WebImage("weburl"),
                            HeaderImage("headerurl")
                    )
            ).just()
        }
    }

    override fun getCollection(id: String): Observable<ArtObjectDetail> {
        return if (id == "") {
            ItemNotFoundException().error<ArtObjectDetail>()
        } else {
            ArtObjectDetail(
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
            ).just()
        }
    }

}