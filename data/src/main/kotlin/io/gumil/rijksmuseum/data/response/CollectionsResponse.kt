package io.gumil.rijksmuseum.data.response

import com.squareup.moshi.Json

data class CollectionResponse(
        @Json(name = "artObjects") val artObjects: List<ArtObject>
)

data class ArtObject(
        @Json(name = "id") val id: String,
        @Json(name = "objectNumber") val objectNumber: String,
        @Json(name = "title") val title: String,
        @Json(name = "principalOrFirstMaker") val principalOrFirstMaker: String,
        @Json(name = "longTitle") val longTitle: String,
        @Json(name = "webImage") val webImage: WebImage,
        @Json(name = "headerImage") val headerImage: HeaderImage,
        @Json(name = "productionPlaces") val productionPlaces: List<String>
)

data class HeaderImage(
        @Json(name = "guid") val guid: String,
        @Json(name = "width") val width: Int,
        @Json(name = "height") val height: Int,
        @Json(name = "url") val url: String
)

data class WebImage(
        @Json(name = "guid") val guid: String,
        @Json(name = "offsetPercentageX") val offsetPercentageX: Int,
        @Json(name = "offsetPercentageY") val offsetPercentageY: Int,
        @Json(name = "width") val width: Int,
        @Json(name = "height") val height: Int,
        @Json(name = "url") val url: String
)