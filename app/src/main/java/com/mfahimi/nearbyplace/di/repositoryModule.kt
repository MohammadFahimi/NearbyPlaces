package com.mfahimi.nearbyplace.di

import com.mfahimi.nearbyplace.data.repositoty.detail.LocationDetailRepo
import com.mfahimi.nearbyplace.data.repositoty.detail.LocationDetailRepositoryImpl
import com.mfahimi.nearbyplace.data.repositoty.list.LocationListRepo
import com.mfahimi.nearbyplace.data.repositoty.list.LocationListRepositoryImpl
import org.koin.dsl.module

val repositoryModule = module {
    factory<LocationListRepo> { LocationListRepositoryImpl(get(), get(), get(),get()) }
    factory<LocationDetailRepo> { LocationDetailRepositoryImpl(get(), get(), get(),get()) }
}