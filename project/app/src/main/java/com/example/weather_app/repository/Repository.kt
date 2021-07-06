package com.example.weather_app.repository

import com.example.weather_app.webservices.model.current_weather_data.CurrentWeatherDataResponse
import retrofit2.Response

interface Repository {

    suspend fun getCurrentWeatherDataResponse(
        apiKey: String,
        forecastLocation: String,
        unitsSystem: String
    ) : Response<CurrentWeatherDataResponse>

}
