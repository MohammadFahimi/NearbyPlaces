package com.mfahimi.nearbyplace.data.network



interface ApiExecutor {
    suspend fun <T> executeApi(request: suspend () -> RetroResponse<T>): Response<T>
}