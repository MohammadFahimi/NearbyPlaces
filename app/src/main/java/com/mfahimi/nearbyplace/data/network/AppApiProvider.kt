package com.mfahimi.nearbyplace.data.network

import com.mfahimi.nearbyplace.app.AppConfig
import com.mfahimi.nearbyplace.data.network.interceptor.CancelableInterceptor
import com.mfahimi.nearbyplace.data.network.interceptor.HeaderInterceptor
import com.bityon.data.model.network.interceptor.LoggingInterceptor
import com.mfahimi.nearbyplace.app.Constants
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit


class AppApiProvider(appConfig: AppConfig, headerProvider: (() -> Map<String, String?>?)? = null) {
    val retrofit: Retrofit

    init {
        val client = OkHttpClient.Builder().apply {
            hostnameVerifier { hostname, session ->
                appConfig.isSecureHost(hostname)
            }
            connectTimeout(Constants.API.API_REQ_CONNECTION_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)
            readTimeout(Constants.API.API_REQ_READ_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)
            writeTimeout(Constants.API.API_REQ_WRITE_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)
            addInterceptor(CancelableInterceptor())
            if (headerProvider != null) {
                addInterceptor(HeaderInterceptor(headerProvider))
            }
            if (appConfig.isInDebugMode()) {
                addInterceptor(LoggingInterceptor())
                addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                })
            }
        }.build()

        retrofit = Retrofit.Builder()
                .baseUrl(appConfig.getBaseUrl())
                .addConverterFactory(MoshiConverterFactory.create(Moshi.Builder().build()))
                .client(client)
                .build()
    }
}
