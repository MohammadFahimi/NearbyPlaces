package com.mfahimi.nearbyplace.ext

import com.mfahimi.nearbyplace.data.network.Response
import com.mfahimi.nearbyplace.data.network.exceptions.NetworkException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach

inline fun <T> Flow<Response<T>?>.handleApiResponse(
    crossinline onLoad: () -> Unit,
    crossinline onSuccess: (T) -> Unit,
    crossinline onError: ((error: NetworkException) -> Unit)
):Flow<Response<T>?> {
   return this.onEach {
        when (it) {
            is Response.Idle -> {
            }
            is Response.Loading -> onLoad()
            is Response.Success -> onSuccess(it.data)
            is Response.Error -> onError(it.error)
        }
    }
}