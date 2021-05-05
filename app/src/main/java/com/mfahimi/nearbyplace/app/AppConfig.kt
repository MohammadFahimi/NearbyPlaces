package com.mfahimi.nearbyplace.app

interface AppConfig {

    fun isLogEnabled(): Boolean

    fun isInDebugMode(): Boolean

    fun getBaseUrl(): String

    fun getApplicationId(): String

    fun getVersionCode(): Int

    fun getVersionName(): String

    fun isSecureHost(host: String): Boolean
}