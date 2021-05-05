package com.mfahimi.nearbyplace.data.network.exceptions

import com.mfahimi.nearbyplace.data.network.HttpStatusCode
import org.json.JSONException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException

sealed class NetworkException(message: String?) : Throwable(message) {
    class DisconnectedError() : NetworkException("Disconnected")
    class ParseError() : NetworkException("ParseError")
    class TimeoutError() : NetworkException("TimeoutError")
    class UnknownError(message: String?) : NetworkException(message ?: "UnknownError")
    class CustomError(message: String?) : NetworkException(message ?: "CustomError")
    class BadRequestError(message: String?) : NetworkException(message)
    class ServerError500(message: String? = null) : NetworkException(message ?: "ServerError500")
    class EmptyResponseError(message: String?) : NetworkException(message ?: "EmptyResponseError")
    class HttpError(code: HttpStatusCode) : NetworkException(code.code.toString())
}

fun Throwable.getApiError(): NetworkException {
    return when (this) {
        is UnknownHostException -> NetworkException.DisconnectedError()
        is JSONException -> NetworkException.ParseError()
        is TimeoutException, is SocketTimeoutException -> NetworkException.TimeoutError()
        else -> NetworkException.UnknownError(this.message)
    }
}