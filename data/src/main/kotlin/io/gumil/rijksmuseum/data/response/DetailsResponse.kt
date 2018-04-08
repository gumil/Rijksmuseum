package io.gumil.rijksmuseum.data.response

import com.squareup.moshi.Json

data class DetailsResponse(
        @Json(name = "artObject") val artObject: ArtObjectDetail?
)

data class ArtObjectDetail(
        @Json(name = "title") val title: String,
        @Json(name = "webImage") val webImage: WebImage,
        @Json(name = "objectTypes") val objectTypes: List<String>?,
        @Json(name = "principalMaker") val principalMaker: String?,
        @Json(name = "materials") val materials: List<String>?,
        @Json(name = "techniques") val techniques: List<String>?,
        @Json(name = "dating") val dating: Dating?,
        @Json(name = "subTitle") val subTitle: String?,
        @Json(name = "scLabelLine") val scLabelLine: String?,
        @Json(name = "label") val label: Label?
)

data class Dating(
        @Json(name = "presentingDate") val presentingDate: String,
        @Json(name = "period") val period: Int
)

data class Label(
        @Json(name = "description") val description: String?
)

enum class LinkType {
    TYPE, MAKER, PERIOD, MATERIAL, TECHNIQUE;

    override fun toString(): String {
        return when(this) {
            LinkType.MAKER -> "principalMaker"
            LinkType.PERIOD -> "f.dating.period"
            else -> this.name.toLowerCase()
        }
    }
}