package com.example.weather_app.repository

import com.example.weather_app.database.entities.CityShortcut
import com.example.weather_app.webservices.entities.currentweatherdata.CurrentWeatherDataResponse
import com.example.weather_app.webservices.entities.weatherforecastdata.WeatherForecastDataResponse
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
