package io.gumil.rijksmuseum

import android.annotation.SuppressLint
import android.os.Parcelable
import io.gumil.rijksmuseum.data.response.ArtObject
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
internal data class CollectionItem(
        val title: String,
        val image: String
) : Parcelable


internal fun ArtObject.mapToItem(): CollectionItem {
    return CollectionItem(title, headerImage.url)
}