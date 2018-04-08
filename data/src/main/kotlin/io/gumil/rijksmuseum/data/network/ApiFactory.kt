package io.gumil.rijksmuseum.data.network

import android.content.Context
import android.net.ConnectivityManager
import com.squareup.moshi.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi
import okhttp3.Cache
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

internal object ApiFactory {

    private const val BASE_URL = "https://www.rijksmuseum.nl/"
    private const val CACHE_SIZE: Long = 10 * 1024 * 1024

    fun create(context: Context?, isDebug: Boolean, url: String = BASE_URL) =
            createRijksmuseumApi(url, createOkHttpClient(context, createLoggingInterceptor(isDebug)))

    private fun createRijksmuseumApi(url: String, okHttpClient: OkHttpClient): RijksmuseumApi =
            Retrofit.Builder()
                    .baseUrl(url)
                    .client(okHttpClient)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(createConverter())
                    .build().create(RijksmuseumApi::class.java)

    private fun createOkHttpClient(context: Context?, httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient =
            OkHttpClient.Builder()
                    .addNetworkInterceptor(cacheResponseInterceptor())
                    .apply {
                        context?.let {
                            cache(Cache(it.cacheDir, CACHE_SIZE))
                            addInterceptor(cacheInterceptor(it))
                        }
                    }
                    .addInterceptor(httpLoggingInterceptor)
                    .connectTimeout(20, TimeUnit.SECONDS)
                    .readTimeout(20, TimeUnit.SECONDS)
                    .build()

    private fun createLoggingInterceptor(isDebug: Boolean): HttpLoggingInterceptor =
            HttpLoggingInterceptor().apply {
                level = if (isDebug) {
                    HttpLoggingInterceptor.Level.BODY
                } else {
                    HttpLoggingInterceptor.Level.NONE
                }
            }

    private fun cacheInterceptor(context: Context): Interceptor = Interceptor { chain ->
        chain.proceed(chain.request()
                .newBuilder()
                .apply {
                    if (!context.isNetworkAvailable()) {
                        cacheControl(CacheControl.FORCE_CACHE)
                    }
                }
                .build())
    }

    private fun cacheResponseInterceptor() = Interceptor {
        it.proceed(it.request()).newBuilder()
                .removeHeader("Pragma")
                .removeHeader("Cache-Control")
                .header("Cache-Control", "public, max-age=120, max-stale=${Integer.MAX_VALUE}")
                .build()
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

    private fun Context.isNetworkAvailable(): Boolean {
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting
    }
}

