package com.itechevo.data.source

import com.itechevo.data.model.CakeResponse
import retrofit2.http.GET
import retrofit2.http.Url

interface WaracleApiService {

    //TODO: Remove hardcoded url prefix
    @GET
    suspend fun getCakes(@Url url: String = "waracle_cake-android-client"): List<CakeResponse>

    companion object {
        const val BASE_URL =
            "https://gist.githubusercontent.com/t-reed/739df99e9d96700f17604a3971e701fa/raw/1d4dd9c5a0ec758ff5ae92b7b13fe4d57d34e1dc/"
    }
}