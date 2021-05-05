package com.mfahimi.nearbyplace.data.network.interceptor

import okhttp3.Call
import okhttp3.Interceptor
import okhttp3.Response
import java.lang.ref.WeakReference
import java.util.concurrent.ConcurrentHashMap

class CancelableInterceptor : Interceptor {

    //Here we cache last call to be able cancel requests
    private val requestMap = ConcurrentHashMap<String, WeakReference<Call>>()

    override fun intercept(chain: Interceptor.Chain): Response {
        cancelOngoingRequest(chain.call())
        return chain.proceed(chain.request())
    }

    @Synchronized
    private fun cancelOngoingRequest(call: Call) {
        val url = call.request().url().toString()
        requestMap.remove(url)?.get()?.cancel()
        requestMap[url] = WeakReference(call)
    }
}