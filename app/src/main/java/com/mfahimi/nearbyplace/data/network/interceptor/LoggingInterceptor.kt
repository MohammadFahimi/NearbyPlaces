package com.bityon.data.model.network.interceptor

import okhttp3.FormBody
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody


class LoggingInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val reqBody = request.body()
        val reqUrl = "Url: ${request.url()}"
        val reqMethod = "Method: ${request.method()}"
        var reqNameValues = "Body: "
        var reqHeaders = "Headers: " + if (request.headers().size() > 0) "" else "-"

        for (i in 0 until request.headers().size()) {
            reqHeaders += "[${request.headers().name(i)}:${request.headers().value(i)}]"
        }

        if (reqBody is FormBody) {
            if (reqBody.size() > 0) {
                for (i in 0 until reqBody.size()) {
                    reqNameValues += "[${reqBody.name(i)}: ${reqBody.value(i)}]"
                    if (i != reqBody.size() - 1) {
                        reqNameValues += ", "
                    }
                }
            } else {
                reqNameValues += "-"
            }
        }
        val t1 = System.nanoTime()
        val response = chain.proceed(request)
        val t2 = System.nanoTime()
        val dtMillis = (t2 - t1) / 1e6
        val responseBytes = response.body()?.bytes()
        val responseString = responseBytes?.run { String(this) } ?: "-"
        val responseContentType = response.body()?.contentType()

        //Response is consumed, so we create another one
        return response.newBuilder()
            .body(ResponseBody.create(responseContentType, responseString))
            .build()

    }
}