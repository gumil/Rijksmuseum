package io.gumil.rijksmuseum.data.response

import com.squareup.moshi.Json

data class DetailsResponse(
        @Json(name = "elapsedMilliseconds") val elapsedMilliseconds: Int,
        @Json(name = "artObject") val artObject: ArtObjectDetail,
        @Json(name = "artObjectPage") val artObjectPage: ArtObjectPage
)

data class ArtObjectPage(
        @Json(name = "id") val id: String,
        @Json(name = "lang") val lang: String,
        @Json(name = "objectNumber") val objectNumber: String,
        @Json(name = "plaqueDescription") val plaqueDescription: String,
        @Json(name = "createdOn") val createdOn: String,
        @Json(name = "updatedOn") val updatedOn: String
)

data class ArtObjectDetail(
        @Json(name = "id") val id: String,
        @Json(name = "priref") val priref: String,
        @Json(name = "objectNumber") val objectNumber: String,
        @Json(name = "language") val language: String,
        @Json(name = "title") val title: String,
        @Json(name = "webImage") val webImage: WebImage,
        @Json(name = "titles") val titles: List<String>,
        @Json(name = "description") val description: String,
        @Json(name = "objectTypes") val objectTypes: List<String>,
        @Json(name = "objectCollection") val objectCollection: List<String>,
        @Json(name = "principalMakers") val principalMakers: List<PrincipalMaker>,
        @Json(name = "plaqueDescriptionDutch") val plaqueDescriptionDutch: String,
        @Json(name = "plaqueDescriptionEnglish") val plaqueDescriptionEnglish: String,
        @Json(name = "principalMaker") val principalMaker: String,
        @Json(name = "acquisition") val acquisition: Acquisition,
        @Json(name = "materials") val materials: List<String>,
        @Json(name = "dating") val dating: Dating,
        @Json(name = "classification") val classification: Classification,
        @Json(name = "hasImage") val hasImage: Boolean,
        @Json(name = "documentation") val documentation: List<String>,
        @Json(name = "principalOrFirstMaker") val principalOrFirstMaker: String,
        @Json(name = "dimensions") val dimensions: List<Dimension>,
        @Json(name = "physicalMedium") val physicalMedium: String,
        @Json(name = "longTitle") val longTitle: String,
        @Json(name = "subTitle") val subTitle: String,
        @Json(name = "scLabelLine") val scLabelLine: String,
        @Json(name = "label") val label: Label,
        @Json(name = "showImage") val showImage: Boolean,
        @Json(name = "location") val location: String
)

data class Dimension(
        @Json(name = "unit") val unit: String,
        @Json(name = "type") val type: String,
        @Json(name = "value") val value: String
)

data class Dating(
        @Json(name = "presentingDate") val presentingDate: String,
        @Json(name = "sortingDate") val sortingDate: Int,
        @Json(name = "period") val period: Int
)

data class Acquisition(
        @Json(name = "method") val method: String?,
        @Json(name = "date") val date: String
)

data class Classification(
        @Json(name = "iconClassIdentifier") val iconClassIdentifier: List<String>,
        @Json(name = "iconClassDescription") val iconClassDescription: List<String>,
        @Json(name = "objectNumbers") val objectNumbers: List<String>
)

data class Label(
        @Json(name = "title") val title: String,
        @Json(name = "makerLine") val makerLine: String,
        @Json(name = "description") val description: String,
        @Json(name = "date") val date: String
)

data class PrincipalMaker(
        @Json(name = "name") val name: String,
        @Json(name = "unFixedName") val unFixedName: String,
        @Json(name = "placeOfBirth") val placeOfBirth: String?,
        @Json(name = "dateOfBirth") val dateOfBirth: String?,
        @Json(name = "dateOfDeath") val dateOfDeath: String?,
        @Json(name = "placeOfDeath") val placeOfDeath: String?,
        @Json(name = "occupation") val occupation: List<String>,
        @Json(name = "roles") val roles: List<String>
)

