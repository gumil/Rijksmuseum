package io.gumil.rijksmuseum.data.repository

import io.gumil.rijksmuseum.data.response.ArtObject
import io.gumil.rijksmuseum.data.response.ArtObjectDetail
import io.gumil.rijksmuseum.data.response.LinkType
import io.reactivex.Observable

interface RijksRepository {

    fun getCollections(page: Int = 1, param: Pair<LinkType, String>? = null): Observable<List<ArtObject>>

    fun getCollection(id: String): Observable<ArtObjectDetail>

}