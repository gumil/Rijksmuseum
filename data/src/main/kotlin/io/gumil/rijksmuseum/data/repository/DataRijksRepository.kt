package io.gumil.rijksmuseum.data.repository

import io.gumil.rijksmuseum.data.network.RijksmuseumApi
import io.gumil.rijksmuseum.data.response.ArtObject
import io.gumil.rijksmuseum.data.response.ArtObjectDetail
import io.gumil.rijksmuseum.data.util.applySchedulers
import io.reactivex.Observable

internal class DataRijksRepository(
        private val api: RijksmuseumApi
) : RijksRepository {
    override fun getCollections(page: Int): Observable<List<ArtObject>> {
        return api.getCollections(page = page)
                .map {
                    it.artObjects
                }
                .toObservable()
                .applySchedulers()

    }

    override fun getCollection(id: String): Observable<ArtObjectDetail> {
        return api.getCollection(id)
                .map {
                    it.artObject
                }
                .toObservable()
                .applySchedulers()
    }
}