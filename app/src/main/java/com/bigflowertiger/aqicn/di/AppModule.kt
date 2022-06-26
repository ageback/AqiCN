package com.bigflowertiger.aqicn.di

import com.bigflowertiger.aqicn.domain.AqicnRepository
import com.bigflowertiger.aqicn.model.AqicnResponse
import com.bigflowertiger.aqicn.network.AQICN_BASE_URL
import com.bigflowertiger.aqicn.network.AqicnService
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideAqiRetrofit(
        gson: Gson
    ): Retrofit = Retrofit.Builder()
        .baseUrl(AQICN_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    @Provides
    fun provideGson(): Gson = GsonBuilder().create()

    @Provides
    fun provideAqicnRepository(apiService: AqicnService) = AqicnRepository(apiService)

    @Provides
    fun provideAqicnService(retrofit: Retrofit): AqicnService =
        retrofit.create(AqicnService::class.java)
}