package com.example.weather_app.repository

import com.example.weather_app.webservices.OpenWeatherAPIService
import com.example.weather_app.webservices.model.current_weather_data.CurrentWeatherDataResponse
import retrofit2.Response

class RepositoryImpl(
    private val webservice: OpenWeatherAPIService
) : Repository {

    override suspend fun getCurrentWeatherDataResponse(
        apiKey: String,
        forecastLocation: String,
        unitsSystem: String
    ): Response<CurrentWeatherDataResponse> = webservice.getCurrentWeatherData(
        forecastLocation,
        apiKey,
        unitsSystem
    )
}