package com.mfahimi.nearbyplace.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [LocationData::class, LocationDetail::class], version = 3, exportSchema = true)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getLocationListDB(): LocationDao
    abstract fun getLocationDetailDB(): LocationDetailDao
}