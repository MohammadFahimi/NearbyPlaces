package com.mfahimi.nearbyplace.data.model.locationList


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class JResLocationList(
        @Json(name = "meta")
        val meta: Meta,
        @Json(name = "response")
        val response: Response
) {
    @JsonClass(generateAdapter = true)
    data class Meta(
            @Json(name = "code")
            val code: Int,
            @Json(name = "requestId")
            val requestId: String
    )

    @JsonClass(generateAdapter = true)
    data class Response(
            @Json(name = "groups")
            val groups: List<Group>,
            @Json(name = "headerFullLocation")
            val headerFullLocation: String,
            @Json(name = "headerLocation")
            val headerLocation: String,
            @Json(name = "headerLocationGranularity")
            val headerLocationGranularity: String,
            @Json(name = "suggestedRadius")
            val suggestedRadius: Int,
            @Json(name = "totalResults")
            val totalResults: Int
    ) {
        @JsonClass(generateAdapter = true)
        data class Group(
                @Json(name = "items")
                val items: List<Item>
        ) {
            @JsonClass(generateAdapter = true)
            data class Item(
                    @Json(name = "venue")
                    val venue: Venue
            ) {
                @JsonClass(generateAdapter = true)
                data class Venue(
                        @Json(name = "id")
                        val id: String,
                        @Json(name = "location")
                        val location: Location,
                        @Json(name = "name")
                        val name: String
                ) {

                    @JsonClass(generateAdapter = true)
                    data class Location(
                            @Json(name = "address")
                            val address: String?,
                            @Json(name = "city")
                            val city: String?,
                            @Json(name = "country")
                            val country: String?,
                            @Json(name = "distance")
                            val distance: Int,
                            @Json(name = "lat")
                            val lat: Double,
                            @Json(name = "lng")
                            val lng: Double,
                    )
                }
            }
        }
    }
}