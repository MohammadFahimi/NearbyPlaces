package com.mfahimi.nearbyplace.data.repositoty.detail

import com.mfahimi.nearbyplace.data.network.Response
import com.mfahimi.nearbyplace.model.LocationDetailItem
import kotlinx.coroutines.flow.Flow

interface LocationDetailRepo {
    fun getVenueDetail(locationId: String): Flow<Response<LocationDetailItem>>
}