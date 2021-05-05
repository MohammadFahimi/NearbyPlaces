package com.mfahimi.nearbyplace.data.repositoty.detail

import com.mfahimi.nearbyplace.app.TokenProvider
import com.mfahimi.nearbyplace.data.local.AppDatabase
import com.mfahimi.nearbyplace.data.local.LocationDetail
import com.mfahimi.nearbyplace.data.model.locationDetail.JResLocationDetail
import com.mfahimi.nearbyplace.data.network.ApiExecutor
import com.mfahimi.nearbyplace.data.network.NetworkBoundResource
import com.mfahimi.nearbyplace.data.network.Response
import com.mfahimi.nearbyplace.data.network.RetroResponse
import com.mfahimi.nearbyplace.data.network.apiService.ApiService
import com.mfahimi.nearbyplace.model.LocationDetailItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class LocationDetailRepositoryImpl constructor(
    val apiService: ApiService,
    val database: AppDatabase,
    val apiExecutor: ApiExecutor,
    val tokenProvider: TokenProvider,
) : LocationDetailRepo {

    override fun getVenueDetail(locationId: String): Flow<Response<LocationDetailItem>> = flow {
        object : NetworkBoundResource<LocationDetailItem, JResLocationDetail>(apiExecutor) {
            override suspend fun setValue(newValue: Response<LocationDetailItem>) {
                emit(newValue)
            }

            override fun shouldFetch(data: LocationDetailItem?): Boolean = data == null

            override suspend fun loadFromDb(): LocationDetailItem? =
                //todo(move mapping to another class)
                database.getLocationDetailDB().getVenueDetail(locationId)?.let {
                    LocationDetailItem(
                        it.locationId, it.name, it.address, it.city, it.lat, it.lng,it.createData
                    )
                }

            override suspend fun createCall(): RetroResponse<JResLocationDetail> =
                apiService.getDetailSuspend(
                    venue_Id = locationId,
                    clientID = tokenProvider.clientId,
                    clientSecret = tokenProvider.secretId
                )

            override suspend fun saveCallResult(item: JResLocationDetail) {
                //todo (move mapping to another class)
                database.getLocationDetailDB().insert(
                    LocationDetail(
                        locationId = item.response.venue.id,
                        name = item.response.venue.name,
                        city = item.response.venue.location.city,
                        address = item.response.venue.location.address,
                        lat = item.response.venue.location.lat,
                        lng = item.response.venue.location.lng,
                        createData = item.response.venue.createdAt,
                    )
                )
            }
        }.start()
    }

}