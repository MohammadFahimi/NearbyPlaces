package com.mfahimi.nearbyplace.app


class AppConfigImpl(
    private val isDebugMode: Boolean, private val baseUrl: String,
    private val versionCode: Int, private val versionName: String,
    private val applicationID: String
) : AppConfig {

    override fun isLogEnabled(): Boolean {
        return !isDebugMode
    }

    override fun isInDebugMode(): Boolean = isDebugMode

    override fun getBaseUrl(): String = baseUrl

    override fun getVersionName(): String = versionName

    override fun getApplicationId(): String = applicationID

    override fun getVersionCode(): Int = versionCode

    override fun isSecureHost(host: String): Boolean {
        return listOf(baseUrl).any {
            it.contains(host)
        }
    }
}