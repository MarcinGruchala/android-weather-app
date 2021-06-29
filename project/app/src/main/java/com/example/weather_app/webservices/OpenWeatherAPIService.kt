package com.example.weather_app.webservices

import com.example.weather_app.webservices.model.CurrentWeatherDataResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherAPIService {

    @GET("weather")
    suspend fun getCurrentWeatherData(
        @Query("q") city: String,
        @Query("appid") key: String
    ) : Response<CurrentWeatherDataResponse>
}