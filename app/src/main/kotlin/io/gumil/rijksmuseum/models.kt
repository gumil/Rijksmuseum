package io.gumil.rijksmuseum

import android.annotation.SuppressLint
import android.os.Parcelable
import io.gumil.rijksmuseum.data.response.ArtObject
import io.gumil.rijksmuseum.data.response.ArtObjectDetail
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
internal data class CollectionItem(
        val id: String,
        val title: String,
        val image: String
) : Parcelable

@SuppressLint("ParcelCreator")
@Parcelize
internal data class CollectionDetailItem(
        val title: String,
        val image: String,
        val width: Int = -1,
        val height: Int = -1
) : Parcelable

internal fun ArtObject.mapToItem(): CollectionItem {
    return CollectionItem(objectNumber, title, headerImage.url)
}

internal fun ArtObjectDetail.mapToItem(): CollectionDetailItem {
    return CollectionDetailItem(title, webImage.url, webImage.width, webImage.height)
}