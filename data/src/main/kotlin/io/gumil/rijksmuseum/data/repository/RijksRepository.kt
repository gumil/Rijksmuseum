package io.gumil.rijksmuseum.data.repository

import io.gumil.rijksmuseum.data.response.ArtObject
import io.gumil.rijksmuseum.data.response.ArtObjectDetail
import io.reactivex.Observable

interface RijksRepository {

    fun getCollections(page: Int = 1): Observable<List<ArtObject>>

    fun getCollection(id: String): Observable<ArtObjectDetail>

}