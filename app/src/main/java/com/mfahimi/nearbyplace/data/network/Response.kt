package com.mfahimi.nearbyplace.data.network

import com.mfahimi.nearbyplace.data.network.exceptions.NetworkException

sealed class Response<out T> {
    object Idle: Response<Nothing>()
    data class Loading<out T>(val data: T?) : Response<T>()
    data class Success<out T>(val data: T) : Response<T>()
    data class Error(val error: NetworkException) : Response<Nothing>()
}