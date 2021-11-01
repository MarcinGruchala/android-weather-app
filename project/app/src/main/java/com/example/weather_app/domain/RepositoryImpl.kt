package com.example.weather_app.domain

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.weather_app.persistence.dao.CityShortcutDao
import com.example.weather_app.persistence.entities.CityShortcut
import com.example.weather_app.networking.OpenWeatherAPIService
import com.example.weather_app.networking.entities.currentweatherdata.CurrentWeatherDataResponse
import com.example.weather_app.networking.entities.weatherforecastdata.WeatherForecastDataResponse
import retrofit2.Response
import java.util.*


class RepositoryImpl(
    private val webservice: OpenWeatherAPIService,
    private val cityShortcutDao: CityShortcutDao,
    unitOfMeasurementSP: SharedPreferences
) : Repository {

    var deviceTimezone = TimeZone.getDefault().rawOffset
    var isTimezoneSet = false

    val deviceLocation: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val unitOfMeasurement: MutableLiveData<String> by lazy {
        MutableLiveData<String>(UnitOfMeasurement.METRIC.value)
    }

    val mainForecastLocation: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val allCityShortcutList: LiveData<List<CityShortcut>> by lazy {
        cityShortcutDao.getAllCityShortcuts()
    }

    init {
        unitOfMeasurement.value = unitOfMeasurementSP.getString(
            "unitOfMeasurement",
            UnitOfMeasurement.METRIC.value
        )
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

    override suspend fun addCityShortcutToDatabase(
        cityShortcut: CityShortcut
    ) {
        cityShortcutDao.addCityShortcut(cityShortcut)
    }

    override suspend fun deleteCityShortcutFromDatabase(
        cityShortcut: CityShortcut
    ) {
        cityShortcutDao.deleteCityShortcut(cityShortcut)
    }
}
