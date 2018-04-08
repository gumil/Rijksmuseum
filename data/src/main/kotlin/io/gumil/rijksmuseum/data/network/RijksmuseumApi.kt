package io.gumil.rijksmuseum.data.network

import io.gumil.rijksmuseum.data.BuildConfig
import io.gumil.rijksmuseum.data.response.CollectionResponse
import io.gumil.rijksmuseum.data.response.DetailsResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

internal interface RijksmuseumApi {

    @GET("/api/{lang}/collection?key=${BuildConfig.API_KEY}&format=json&imgonly=true")
    fun getCollections(@Path("lang") lang: String = "en", @Query("p") page: Int = 1): Single<CollectionResponse>

    @GET("/api/{lang}/collection/{id}?key=${BuildConfig.API_KEY}&format=json")
    fun getCollection(@Path("id") id: String,
                      @Path("lang") lang: String = "en"): Single<DetailsResponse>
}