package com.mfahimi.nearbyplace.data.repositoty.list

import android.location.Location
import com.mfahimi.nearbyplace.app.Constants
import com.mfahimi.nearbyplace.app.TokenProvider
import com.mfahimi.nearbyplace.data.local.AppDatabase
import com.mfahimi.nearbyplace.data.local.LocationData
import com.mfahimi.nearbyplace.data.model.locationList.JResLocationList
import com.mfahimi.nearbyplace.data.network.ApiExecutor
import com.mfahimi.nearbyplace.data.network.NetworkBoundResource
import com.mfahimi.nearbyplace.data.network.Response
import com.mfahimi.nearbyplace.data.network.RetroResponse
import com.mfahimi.nearbyplace.data.network.apiService.ApiService
import com.mfahimi.nearbyplace.model.LocationDataItem
import com.mfahimi.nearbyplace.util.LocationUtil
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class LocationListRepositoryImpl constructor(
    val apiExecutor: ApiExecutor,
    val tokenProvider: TokenProvider,
    private val apiService: ApiService,
    private val database: AppDatabase,
) : LocationListRepo {
    companion object {
        private const val DEFAULT_NETWORK_PAGE_SIZE = Constants.Paging.PAGE_SIZE
    }

    override fun getLocationList(
        pageIndex: Int,
        lat: Double,
        lng: Double
    ): Flow<Response<List<LocationDataItem>>> = flow {
        object : NetworkBoundResource<List<LocationDataItem>, JResLocationList>(apiExecutor) {
            override suspend fun setValue(newValue: Response<List<LocationDataItem>>) {
                emit(newValue)
            }

            override suspend fun saveCallResult(item: JResLocationList) {
                if (pageIndex == 0)
                    database.getLocationListDB().clearTable()
                //todo(move mapping to mapper class)
                val data = item.response.groups[0].items.map {
                    LocationData(
                        id = it.venue.id,
                        name = it.venue.name,
                        address = it.venue.location.address,
                        city = it.venue.location.city,
                        lat = it.venue.location.lat,
                        lng = it.venue.location.lng,
                    )
                }
                database.getLocationListDB().insertAll(data)
            }

            override fun shouldFetch(data: List<LocationDataItem>?): Boolean {
                if (data.isNullOrEmpty())
                    return true
                val distance = data.get(0).let { LocationUtil.getDistanceBetweenLocations(it.lat, it.lng, lat, lng) }
                return distance > 100.0
            }

            override suspend fun loadFromDb(): List<LocationDataItem> {
                return database.getLocationListDB()
                    .getDataByPageIndex(pageIndex, DEFAULT_NETWORK_PAGE_SIZE)
                    .map {//todo(move it to mapper)
                        LocationDataItem(
                            id = it.id,
                            name = it.name,
                            address = it.address,
                            city = it.city,
                            lat = it.lat,
                            lng = it.lng
                        )
                    }
            }

            override suspend fun createCall(): RetroResponse<JResLocationList> {
                return apiService.explore(
                    tokenProvider.clientId, tokenProvider.secretId,
                    DEFAULT_NETWORK_PAGE_SIZE,
                    pageIndex * DEFAULT_NETWORK_PAGE_SIZE,
                    "$lat,$lng"
                )
            }
        }.start()
    }
}