package com.mfahimi.nearbyplace.data.model.locationDetail


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class JResLocationDetail(
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
        @Json(name = "venue")
        val venue: Venue
    ) {
        @JsonClass(generateAdapter = true)
        data class Venue(
            @Json(name = "createdAt")
            val createdAt: Long,
            @Json(name = "id")
            val id: String,
            @Json(name = "location")
            val location: Location,
            @Json(name = "name")
            val name: String,
            @Json(name = "timeZone")
            val timeZone: String,
        ) {
            @JsonClass(generateAdapter = true)
            data class Location(
                @Json(name = "address")
                val address: String="",
                @Json(name = "city")
                val city: String="",
                @Json(name = "country")
                val country: String="",
                @Json(name = "lat")
                val lat: Double,
                @Json(name = "lng")
                val lng: Double,
            )

        }
    }
}