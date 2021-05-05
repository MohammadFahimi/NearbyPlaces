package com.mfahimi.nearbyplace.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "LocationData")
class LocationData(
    @PrimaryKey
    val id: String = "",
    val name: String,
    val address: String?,
    val city: String?,
    val lat: Double,
    val lng: Double
)