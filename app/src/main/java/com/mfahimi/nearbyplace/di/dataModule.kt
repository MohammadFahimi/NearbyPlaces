package com.mfahimi.nearbyplace.di

import androidx.room.Room
import com.mfahimi.nearbyplace.data.local.AppDatabase
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val dataModule = module {
    single<AppDatabase> {
        Room
            .databaseBuilder(androidApplication(), AppDatabase::class.java, "applicationdb.db")
            .fallbackToDestructiveMigration()
            .build()
    }
}