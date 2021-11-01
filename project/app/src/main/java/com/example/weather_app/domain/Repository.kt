package com.example.weather_app.domain

import com.example.weather_app.persistence.entities.CityShortcut
import com.example.weather_app.networking.entities.currentweatherdata.CurrentWeatherDataResponse
import com.example.weather_app.networking.entities.weatherforecastdata.WeatherForecastDataResponse
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
