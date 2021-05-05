package com.mfahimi.nearbyplace.data.repositoty.list

import com.mfahimi.nearbyplace.data.network.Response
import com.mfahimi.nearbyplace.model.LocationDataItem
import kotlinx.coroutines.flow.Flow

interface LocationListRepo  {
    fun getLocationList(pageIndex: Int, lat: Double, lng: Double): Flow<Response<List<LocationDataItem>>>
}