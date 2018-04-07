package io.gumil.rijksmuseum.common

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.TransitionOptions
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import io.gumil.rijksmuseum.data.util.applySchedulers
import io.gumil.rijksmuseum.data.util.just
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import timber.log.Timber

@SuppressLint("CheckResult")
internal fun ImageView.load(url: String?,
                            requestOptions: (RequestOptions.() -> RequestOptions)? = null,
                            transitionOptions: TransitionOptions<*, Drawable>? = null) {
    url?.let {
        visibility = View.VISIBLE
        Glide.with(context)
                .load(Uri.parse(url))
                .apply {
                    requestOptions?.invoke(RequestOptions())?.let {
                        apply(it)
                    }

                    transitionOptions?.let { transition(it) }
                }
                .into(this)
    } ?: let { visibility = View.GONE }
}

@SuppressLint("CheckResult")
internal fun ImageView.load(url: String?,
                            requestOptions: (RequestOptions.() -> RequestOptions)? = null) {
    load(url, requestOptions, null)
}

@SuppressLint("CheckResult")
internal fun ImageView.load(drawable: Drawable) {
    Glide.with(context)
            .load(drawable)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(this)
}

internal fun ImageView.preLoad(url: String): Disposable {
    return Observable.defer {
        try {
            Glide.with(context)
                    .load(Uri.parse(url))
                    .submit()
                    .get().just()
        } catch (e: InterruptedException) {
            Timber.w(e, "Loading image interrupted")
            Observable.empty<Drawable>()
        }
    }.applySchedulers().subscribe({
        load(it)
    }, {
        Timber.e(it, "Error loading image")
    })
}