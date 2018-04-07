package io.gumil.rijksmuseum.data.network

import com.squareup.moshi.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

object ApiFactory {

    private const val BASE_URL = "https://www.rijksmuseum.nl/"

    fun create(isDebug: Boolean, url: String = BASE_URL) =
            createRijksmuseumApi(url, createOkHttpClient(createLoggingInterceptor(isDebug)))

    private fun createRijksmuseumApi(url: String, okHttpClient: OkHttpClient): RijksmuseumApi =
            Retrofit.Builder()
                    .baseUrl(url)
                    .client(okHttpClient)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(createConverter())
                    .build().create(RijksmuseumApi::class.java)

    private fun createOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient =
            OkHttpClient.Builder()
                    .addInterceptor(httpLoggingInterceptor)
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .build()

    private fun createLoggingInterceptor(isDebug: Boolean): HttpLoggingInterceptor =
            HttpLoggingInterceptor().apply {
                level = if (isDebug) {
                    HttpLoggingInterceptor.Level.BODY
                } else {
                    HttpLoggingInterceptor.Level.NONE
                }
            }

    private fun createConverter(): Converter.Factory {
        return MoshiConverterFactory.create(
                createMoshi())
    }

    private fun createMoshi(): Moshi {
        return Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()
    }
}