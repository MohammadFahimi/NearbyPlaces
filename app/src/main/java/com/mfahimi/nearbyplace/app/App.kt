package com.mfahimi.nearbyplace.app

import android.app.Application
import com.mfahimi.nearbyplace.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.module.Module


class App : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin()
    }

    private fun initKoin() {
        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(getKoinModule())
        }
    }

    private fun getKoinModule(): List<Module> {
        return listOf(appModule, networkModule, dataModule, repositoryModule, viewModelModule)
    }
}