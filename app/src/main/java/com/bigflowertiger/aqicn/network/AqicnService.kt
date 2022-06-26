package com.bigflowertiger.aqicn.network

import com.bigflowertiger.aqicn.model.AqicnResponse
import retrofit2.http.GET
import retrofit2.http.Path

const val AQICN_BASE_URL = "https://api.waqi.info/"
const val AQICN_TOKEN = "8a3b04bc5f44a9a1f7d07774effa49c778b776b6"

interface AqicnService {

    @GET("feed/geo:{lat};{lng}/?token=${AQICN_TOKEN}")
    suspend fun getGeolocalizedFeed(
        @Path("lng") lng: String,
        @Path("lat") lat: String
    ): NetworkResult<AqicnResponse>
}