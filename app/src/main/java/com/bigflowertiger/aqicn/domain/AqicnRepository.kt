package com.bigflowertiger.aqicn.domain

import com.bigflowertiger.aqicn.network.AqicnService
import javax.inject.Singleton

@Singleton
class AqicnRepository(private val apiService: AqicnService) {
    suspend fun getGeolocalizedFeed(lng: String, lat: String) =
        apiService.getGeolocalizedFeed(lng, lat)
}