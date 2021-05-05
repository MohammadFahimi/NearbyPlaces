package com.mfahimi.nearbyplace.model

data class LocationDataItem(
        val id: String ,
        val name:String,
        val address:String?,
        val city:String?,
        val lat:Double,
        val lng:Double
)