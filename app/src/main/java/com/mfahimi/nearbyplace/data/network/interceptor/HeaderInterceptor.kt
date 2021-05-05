package com.mfahimi.nearbyplace.data.network.interceptor

import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor(private val headerProvider: () -> Map<String, String?>?) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()
        val headers = headerProvider()
        if (headers != null)
            for ((k, v) in headers) {
                if (!v.isNullOrBlank()) {
                    builder.addHeader(k, v)
                }
            }
        val request = builder.build()
        return chain.proceed(request)
    }
}