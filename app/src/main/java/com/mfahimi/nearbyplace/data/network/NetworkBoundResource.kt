package com.mfahimi.nearbyplace.data.network

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread

/**
 * A generic class that can provide a resource backed by both the sqlite database and the network.
 *
 * @param <ResultType>
 * @param <RequestType>
</RequestType></ResultType> */
abstract class NetworkBoundResource<ResultType, RequestType>(private val apiExecutor: ApiExecutor) {

    suspend fun start() {
        setValue(Response.Loading(null))
        val dbSource = loadFromDb()
        if (shouldFetch(dbSource)) {
            fetchFromNetwork(dbSource)
        } else {
            setValue(Response.Success(dbSource!!))
        }
    }

    abstract suspend fun setValue(newValue: Response<ResultType>)

    private suspend fun fetchFromNetwork(dbSource: ResultType?) {
        setValue(Response.Loading(dbSource))
        val apiResponse = apiExecutor.executeApi { createCall() }
        when (apiResponse) {
            is Response.Success -> {
                saveCallResult(processResponse(apiResponse))
                setValue(Response.Success(loadFromDb()!!))
            }
            is Response.Error -> {
                onFetchFailed()
                setValue(Response.Error(apiResponse.error))
            }
            else -> throw IllegalStateException()
        }
    }

    protected open fun onFetchFailed() {}

    @WorkerThread
    protected open fun processResponse(response: Response.Success<RequestType>) = response.data

    @WorkerThread
    protected abstract suspend fun saveCallResult(item: RequestType)

    @MainThread
    protected abstract fun shouldFetch(data: ResultType?): Boolean

    @MainThread
    protected abstract suspend fun loadFromDb(): ResultType?

    @MainThread
    protected abstract suspend fun createCall(): RetroResponse<RequestType>
}
