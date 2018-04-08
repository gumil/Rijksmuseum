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
        val image: String,
        val webImage: String
) : Parcelable

@SuppressLint("ParcelCreator")
@Parcelize
internal data class CollectionDetailItem(
        val title: String,
        val image: String
) : Parcelable

internal fun ArtObject.mapToItem(): CollectionItem {
    return CollectionItem(objectNumber, title, headerImage.url, webImage.url)
}

internal fun ArtObjectDetail.mapToItem(): CollectionDetailItem {
    return CollectionDetailItem(title, webImage.url)
}