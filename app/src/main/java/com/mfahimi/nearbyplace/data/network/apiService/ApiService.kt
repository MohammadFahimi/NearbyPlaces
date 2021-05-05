package com.mfahimi.nearbyplace.data.network.apiService

import com.mfahimi.nearbyplace.data.model.locationDetail.JResLocationDetail
import com.mfahimi.nearbyplace.data.model.locationList.JResLocationList
import com.mfahimi.nearbyplace.data.network.RetroResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("venues/explore?sortByDistance=true&v=20190416")
    suspend fun explore(
            @Query("client_id") clientID: String,
            @Query("client_secret") clientSecret: String,
            @Query("limit") limit: Int,
            @Query("offset") offset: Int,
            @Query("ll") ll: String
    ): RetroResponse<JResLocationList>

    @GET("venues/{VENUE_ID}?v=20190416")
    suspend fun getDetailSuspend(
            @Path("VENUE_ID") venue_Id: String,
            @Query("client_id") clientID: String,
            @Query("client_secret") clientSecret: String
    ): RetroResponse<JResLocationDetail>

}