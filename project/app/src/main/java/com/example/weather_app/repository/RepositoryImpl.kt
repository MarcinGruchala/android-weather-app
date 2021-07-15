package com.example.weather_app.repository

import com.example.weather_app.webservices.OpenWeatherAPIService
import com.example.weather_app.webservices.model.current_weather_data.CurrentWeatherDataResponse
import com.example.weather_app.webservices.model.weather_forecast_data.WeatherForecastDataResponse
import retrofit2.Response

class RepositoryImpl(
    private val webservice: OpenWeatherAPIService
) : Repository {

    var weatherForecastLocation: String = "Bydgoszcz"

    override suspend fun getCurrentWeatherDataResponse(
        apiKey: String,
        forecastLocation: String,
        unitsSystem: String
    ): Response<CurrentWeatherDataResponse> = webservice.getCurrentWeatherData(
        forecastLocation,
        apiKey,
        unitsSystem
    )

    override suspend fun getWeatherForecastDataResponse(
        lat: Double,
        lon: Double,
        exclude: String,
        apiKey: String,
        unitsSystem: String
    ): Response<WeatherForecastDataResponse> = webservice.getWeatherForecastData(
        lat,
        lon,
        exclude,
        apiKey,
        unitsSystem
    )
}