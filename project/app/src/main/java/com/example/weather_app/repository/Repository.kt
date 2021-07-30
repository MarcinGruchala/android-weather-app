package com.example.weather_app.repository

import com.example.weather_app.models.entities.CityShortcut
import com.example.weather_app.webservices.model.current_weather_data.CurrentWeatherDataResponse
import com.example.weather_app.webservices.model.weather_forecast_data.WeatherForecastDataResponse
import retrofit2.Response

interface Repository {

    suspend fun getCurrentWeatherDataResponse(
        apiKey: String,
        forecastLocation: String,
        unitsSystem: String
    ) : Response<CurrentWeatherDataResponse>

    suspend fun getWeatherForecastDataResponse(
        lat: Double,
        lon: Double,
        exclude: String,
        apiKey: String,
        unitsSystem: String
    ) : Response<WeatherForecastDataResponse>

    suspend fun addCityShortcutToDatabase(cityShortcut: CityShortcut)

    suspend fun deleteCityShortcutFromDatabase(cityShortcut: CityShortcut)

}
