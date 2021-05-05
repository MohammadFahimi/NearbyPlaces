package com.mfahimi.nearbyplace.model

class LocationDetailItem(
    val id: String = "",
    val name: String,
    val address: String?,
    val city: String?,
    val lat: Double,
    val lng: Double,
    val createData:Long?
)