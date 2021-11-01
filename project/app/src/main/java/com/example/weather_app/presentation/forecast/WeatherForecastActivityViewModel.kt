package com.example.weather_app.presentation.forecast

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather_app.R
import com.example.weather_app.R.string
import com.example.weather_app.application.WeatherApplication
import com.example.weather_app.domain.RepositoryImpl
import com.example.weather_app.networking.entities.currentweatherdata.CurrentWeatherDataResponse
import com.example.weather_app.networking.entities.weatherforecastdata.WeatherForecastDataResponse
import com.example.weather_app.domain.forecast.CurrentWeather
import com.example.weather_app.domain.forecast.DailyForecast
import com.example.weather_app.domain.forecast.HourlyForecast
import com.example.weather_app.domain.settings.UnitOfMeasurement
import com.example.weather_app.domain.forecast.VerticalWeather
import com.example.weather_app.presentation.common.ClockUtils
import com.example.weather_app.presentation.common.UiUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherForecastActivityViewModel @Inject constructor(
    private val repository: RepositoryImpl,
    private val apiKey: String,
    private val application: WeatherApplication
) : ViewModel() {

    val errorStatus: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>(false)
    }

    val currentWeatherData: MutableLiveData<CurrentWeatherDataResponse> by lazy {
        MutableLiveData<CurrentWeatherDataResponse>()
    }

    val weatherForecastData: MutableLiveData<WeatherForecastDataResponse> by lazy {
        MutableLiveData<WeatherForecastDataResponse>()
    }

    private val deviceLocationObserver = Observer<String> {
        viewModelScope.launch {
            if (repository.mainForecastLocation.value != null){
                downloadWeatherData()
            }
        }
    }

    private val weatherForecastLocationObserver = Observer<String> {
        viewModelScope.launch {
            if (repository.mainForecastLocation.value != null){
                downloadWeatherData()
            }
        }
    }

    private val unitOfMeasurementObserver = Observer<String> {
        viewModelScope.launch {
            if (repository.mainForecastLocation.value != null){
                downloadWeatherData()
            }
        }
    }

    init {
        repository.deviceLocation.observeForever(deviceLocationObserver)
        repository.mainForecastLocation.observeForever(weatherForecastLocationObserver)
        repository.unitOfMeasurement.observeForever(unitOfMeasurementObserver)
    }

     suspend fun downloadWeatherData(){
         val currentWeatherDayResponse = repository.getCurrentWeatherDataResponse(
                 apiKey,
                 repository.mainForecastLocation.value!!,
                 repository.unitOfMeasurement.value!!
             )
         if (currentWeatherDayResponse.isSuccessful &&
             currentWeatherDayResponse.body() != null ) {
                 currentWeatherData.value = currentWeatherDayResponse.body()
             if (!repository.isTimezoneSet) {
                 repository.deviceTimezone = currentWeatherData.value!!.timezone
                 repository.isTimezoneSet = true
             }
             val weatherForecastDataResponse = repository.getWeatherForecastDataResponse(
                     currentWeatherData.value?.coord!!.lat,
                     currentWeatherData.value?.coord!!.lon,
                     "current,minutely,alerts",
                     apiKey,
                     repository.unitOfMeasurement.value!!
                 )
             if (weatherForecastDataResponse.isSuccessful &&
                 currentWeatherDayResponse.body() != null) {
                     weatherForecastData.value = weatherForecastDataResponse.body()
             }
         }
    }

    fun updateDeviceLocation(
        location: String
    ) {
        viewModelScope.launch {
            val currentWeatherDataResponse = repository.getCurrentWeatherDataResponse(
                apiKey,
                location,
                repository.unitOfMeasurement.value!!
            )
            if ( currentWeatherDataResponse.isSuccessful &&
                currentWeatherDataResponse.body() != null ) {
                repository.deviceLocation.value = location
                repository.mainForecastLocation.value = location
            } else {
                errorStatus.value = true
            }
        }
    }

    fun getApiCallTime(): String {
        return application.getString(R.string.lastUpdateHeader) +
                ClockUtils.getTimeFromUnixTimestamp(
                    currentWeatherData.value!!.dt * 1000L,
                    repository.deviceTimezone * 1000L,
                    repository.deviceTimezone * 1000L,
                    true,
                    clockPeriodMode = false
                )
    }

    fun getHourlyForecastList(): List<HourlyForecast> {
        val list: MutableList<HourlyForecast> = mutableListOf()
        list.add(
            HourlyForecast(
            application.getString(
                string.hourlyForecastNow
            ),
            currentWeatherData.value?.main?.temp!!.toInt(),
            UiUtils.getWeatherIcon(currentWeatherData.value!!.weather[0].icon)
            )
        )
        for (i in 1..24) {
            list.add(
                HourlyForecast(
                    ClockUtils.getTimeFromUnixTimestamp(
                        weatherForecastData.value!!.hourly[i].dt * 1000L,
                        weatherForecastData.value!!.timezone_offset * 1000L,
                        repository.deviceTimezone * 1000L,
                        false,
                        clockPeriodMode = false
                    ),
                    weatherForecastData.value!!.hourly[i].temp.toInt(),
                    UiUtils.getWeatherIcon(weatherForecastData.value!!.hourly[i].weather[0].icon)
                )
            )
        }
        return list
    }

    fun getVerticalWeatherDataList(): VerticalWeather {
        val dailyForecastList: List<DailyForecast> = getDailyWeatherForecastList()
        val currentWeatherList: List<CurrentWeather> = getCurrentWeatherDataList(
            repository.unitOfMeasurement.value!!
        )
        return VerticalWeather(dailyForecastList,currentWeatherList)
    }

    private fun getDailyWeatherForecastList(): List<DailyForecast> {
        val dailyForecastList: MutableList<DailyForecast> = mutableListOf()
        for (i in 1..7) {
            dailyForecastList.add(
                DailyForecast(
                    ClockUtils.getDayFromUnixTimestamp(
                        weatherForecastData.value!!.daily[i].dt*1000L,
                        currentWeatherData.value!!.timezone * 1000L,
                        repository.deviceTimezone * 1000L,
                    ),
                    weatherForecastData.value!!.daily[i].temp.max.toInt(),
                    weatherForecastData.value!!.daily[i].temp.min.toInt(),
                    UiUtils.getWeatherIcon(
                        weatherForecastData.value!!.daily[i].weather[0].icon
                    )
                )
            )
        }
        return dailyForecastList
    }

    private fun getCurrentWeatherDataList(
        unitOfMeasurement: String
    ): List<CurrentWeather> {
        val weatherDataList = mutableListOf(
            CurrentWeather(
                application.getString(
                    string.sunriseHeader
                ),
                ClockUtils.getTimeFromUnixTimestamp(
                    currentWeatherData.value!!.sys.sunrise * 1000L,
                    currentWeatherData.value!!.timezone * 1000L,
                    repository.deviceTimezone * 1000L,
                    true,
                    clockPeriodMode = false
                ),
                application.getString(
                    string.sunsetHeader
                ),
                ClockUtils.getTimeFromUnixTimestamp(
                    currentWeatherData.value!!.sys.sunset * 1000L,
                    currentWeatherData.value!!.timezone * 1000L ,
                    repository.deviceTimezone * 1000L,
                    true,
                    clockPeriodMode = false
                )
            ),
            CurrentWeather(
                application.getString(
                    string.localTimeHeader
                ),
                ClockUtils.getTimeFromUnixTimestamp(
                    System.currentTimeMillis(),
                    currentWeatherData.value!!.timezone * 1000L,
                    repository.deviceTimezone * 1000L,
                    true,
                    clockPeriodMode = false
                ),
                application.getString(
                    string.feelsLikeHeader
                ),
                application.getString(
                    string.temp,
                    currentWeatherData.value!!.main.feels_like.toInt()
                )
            ),
            CurrentWeather(
                application.getString(
                    string.pressureHeader
                ),
                application.getString(
                    string.pascals,
                    currentWeatherData.value!!.main.pressure
                ),
                application.getString(
                    string.humidityHeader
                ),
                "${currentWeatherData.value!!.main.humidity}%"
            )
        )
        if (unitOfMeasurement == UnitOfMeasurement.METRIC.value) {
            weatherDataList.add(
                CurrentWeather(
                    application.getString(
                        string.windHeader
                    ),
                    application.getString(
                        string.kph,
                        currentWeatherData.value!!.wind.speed.toInt()
                    ),
                    application.getString(
                        string.visibilityHeader
                    ),
                    application.getString(
                        string.meters,
                        currentWeatherData.value!!.visibility
                    )
                )
            )
            return weatherDataList
        }
        weatherDataList.add(
            CurrentWeather(
                application.getString(
                    string.windHeader
                ),
                application.getString(
                    string.miph,
                    currentWeatherData.value!!.wind.speed.toInt()
                ),
                application.getString(
                    string.visibilityHeader
                ),
                application.getString(
                    string.meters,
                    currentWeatherData.value!!.visibility
                )
            )
        )
        return weatherDataList
    }
}
