package com.mfahimi.nearbyplace.data.local

import androidx.room.Dao
import androidx.room.Query

@Dao
abstract class LocationDao : BaseDao<LocationData> {

    @Query("DELETE FROM LocationData")
    abstract suspend fun clearTable()

    @Query("SELECT * FROM LocationData LIMIT :limit OFFSET :offset")
    abstract suspend fun _getData(limit: Int, offset: Int): List<LocationData>

    suspend fun getDataByPageIndex(pageIndex: Int, pageSize: Int): List<LocationData> {
        return _getData(pageSize, pageIndex * pageSize)
    }
}