package com.mfahimi.nearbyplace.data.network

import com.mfahimi.nearbyplace.data.network.exceptions.NetworkException
import com.mfahimi.nearbyplace.data.network.exceptions.getApiError
import retrofit2.Response

/**
 * Common class used by API responses.
 * @param <T> the type of the response object
</T> */
@Suppress("unused") // T is used in extending classes
sealed class ApiResponse<T> {
    companion object {

        fun <T> create(error: Throwable): ApiErrorResponse<T> {
            return ApiErrorResponse(error.getApiError())
        }

        fun <T> create(response: Response<T>): ApiResponse<T> {
            return if (response.isSuccessful) {
                val body = response.body()
                if (body != null && response.code() == 200)
                    ApiSuccessResponse(body)
                else if (body == null || response.code() == 204) {
                    ApiEmptyResponse()
                } else if (response.code() == HttpStatusCode.InternalServerError.code)
                    ApiErrorResponse(NetworkException.ServerError500())
                else
                    ApiErrorResponse(NetworkException.HttpError(HttpStatusCode.fromInt(response.code())))
            } else {
                val msg = response.errorBody()?.string()
                val errorMsg = if (msg.isNullOrEmpty()) {
                    response.message()
                } else {
                    msg
                }
                ApiErrorResponse(NetworkException.CustomError(errorMsg))
            }
        }
    }
}

/**
 * separate class for HTTP 204 responses so that we can make ApiSuccessResponse's body non-null.
 */
class ApiEmptyResponse<T> : ApiResponse<T>()

data class ApiSuccessResponse<T>(val body: T) : ApiResponse<T>()

/**
 * we user error message in case of when we want to show error message that come from server side
 * such as when use enter incorrect password for login or enter incorrect input field
 * otherwise we use just ApiError to handle error
 */
data class ApiErrorResponse<T>(val error: NetworkException) : ApiResponse<T>()
