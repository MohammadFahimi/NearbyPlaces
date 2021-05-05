package com.mfahimi.nearbyplace.di

import com.mfahimi.nearbyplace.data.network.AppApiProvider
import com.mfahimi.nearbyplace.data.network.apiService.ApiService
import org.koin.dsl.module
import retrofit2.Retrofit

val networkModule = module {
    single { AppApiProvider(get()) }
    single { get<AppApiProvider>().retrofit }
    single<ApiService> { get<Retrofit>().create(ApiService::class.java) }
}