package com.mfahimi.nearbyplace.data.local

import androidx.room.Dao
import androidx.room.Query

@Dao
abstract class LocationDetailDao : BaseDao<LocationDetail> {
    @Query("SELECT * FROM LocationDetail WHERE locationId=:locationId")
    abstract suspend fun getVenueDetail(locationId: String): LocationDetail?
}