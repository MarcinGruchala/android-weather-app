package com.example.weather_app.repository

import androidx.lifecycle.MutableLiveData
import com.example.weather_app.models.CityShortcutData
import com.example.weather_app.webservices.OpenWeatherAPIService
import com.example.weather_app.webservices.model.current_weather_data.CurrentWeatherDataResponse
import com.example.weather_app.webservices.model.weather_forecast_data.WeatherForecastDataResponse
import retrofit2.Response
import javax.inject.Singleton


class RepositoryImpl(
    private val webservice: OpenWeatherAPIService
) : Repository {

    val weatherForecastLocation: MutableLiveData<String> by lazy {
        MutableLiveData<String>("Bydgoszcz")
    }

    val citySelectionList: MutableLiveData<MutableList<CityShortcutData>> by lazy {
        MutableLiveData<MutableList<CityShortcutData>>(mutableListOf(
            CityShortcutData("Bydgoszcz","12:53",30),
            CityShortcutData("Wroclaw","12:53",32),
        ))
    }

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