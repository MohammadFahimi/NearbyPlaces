package com.mfahimi.nearbyplace.di

import com.mfahimi.nearbyplace.BuildConfig
import com.mfahimi.nearbyplace.app.AppConfig
import com.mfahimi.nearbyplace.app.AppConfigImpl
import com.mfahimi.nearbyplace.app.TokenProvider
import com.mfahimi.nearbyplace.app.TokenProviderImpl
import com.mfahimi.nearbyplace.data.network.ApiExecutor
import com.mfahimi.nearbyplace.data.network.ApiExecutorImpl
import org.koin.dsl.module

val appModule = module {
    single<ApiExecutor> { ApiExecutorImpl() }
    single<AppConfig> {
        AppConfigImpl(
            BuildConfig.DEBUG,
            BuildConfig.BASE_URL,
            BuildConfig.VERSION_CODE,
            BuildConfig.VERSION_NAME,
            BuildConfig.APPLICATION_ID
        )
    }
    single<TokenProvider> { TokenProviderImpl(BuildConfig.CLIENT_ID, BuildConfig.SECRET_ID) }
}