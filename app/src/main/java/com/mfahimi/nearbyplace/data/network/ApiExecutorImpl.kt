package com.mfahimi.nearbyplace.data.network


import com.mfahimi.nearbyplace.data.network.exceptions.NetworkException

class ApiExecutorImpl() : ApiExecutor {
    override suspend fun <T> executeApi(request: suspend () -> RetroResponse<T>): Response<T> {
        val response: ApiResponse<T> = try {
            ApiResponse.create(request())
        } catch (t: Throwable) {
            t.printStackTrace()
            ApiResponse.create(t)
        }
        return when (response) {
            is ApiSuccessResponse -> Response.Success(response.body)
            is ApiErrorResponse -> Response.Error(response.error)
            is ApiEmptyResponse -> Response.Error(NetworkException.EmptyResponseError(null))
        }
    }
}