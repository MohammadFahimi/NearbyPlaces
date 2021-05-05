package com.mfahimi.nearbyplace.data.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    primaryKeys = ["locationId"],
    tableName = "LocationDetail", foreignKeys = [ForeignKey(
        entity = LocationData::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("locationId"),
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE
    )]
)
class LocationDetail(
    val locationId: String = "",
    val name: String,
    val address: String?,
    val city: String?,
    val lat: Double,
    val lng: Double,
    val createData:Long?
)