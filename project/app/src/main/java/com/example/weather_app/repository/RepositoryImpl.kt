package com.example.weather_app.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.weather_app.models.UnitOfMeasurement
import com.example.weather_app.models.dao.CityShortcutDao
import com.example.weather_app.models.entities.CityShortcut
import com.example.weather_app.webservices.OpenWeatherAPIService
import com.example.weather_app.webservices.model.current_weather_data.CurrentWeatherDataResponse
import com.example.weather_app.webservices.model.weather_forecast_data.WeatherForecastDataResponse
import retrofit2.Response


class RepositoryImpl(
    private val webservice: OpenWeatherAPIService,
    private val cityShortcutDao: CityShortcutDao
) : Repository {

    val deviceLocation: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val unitOfMeasurement: MutableLiveData<UnitOfMeasurement> by lazy {
        MutableLiveData<UnitOfMeasurement>(UnitOfMeasurement.METRIC)
    }

    val mainForecastLocation: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val allCityShortcutList: LiveData<List<CityShortcut>> by lazy {
        cityShortcutDao.getAllCityShortcuts()
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

    override suspend fun addCityShortcutToDatabase(cityShortcut: CityShortcut) {
        cityShortcutDao.addCityShortcut(cityShortcut)
    }
}