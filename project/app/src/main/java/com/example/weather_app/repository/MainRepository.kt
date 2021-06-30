package com.example.weather_app.repository

import com.example.weather_app.BuildConfig
import com.example.weather_app.webservices.OpenWeatherAPIClient

object MainRepository {

    private val webservice: OpenWeatherAPIClient = OpenWeatherAPIClient

    suspend fun getCurrentWeatherDataResponse() = webservice.api.getCurrentWeatherData(
        "Bydgoszcz",
        BuildConfig.APIKEY,
        "metric"
    )

}