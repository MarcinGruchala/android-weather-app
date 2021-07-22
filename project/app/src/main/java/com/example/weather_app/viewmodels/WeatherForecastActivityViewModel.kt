package com.example.weather_app.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather_app.R
import com.example.weather_app.models.*
import com.example.weather_app.repository.RepositoryImpl
import com.example.weather_app.utils.ClockUtils
import com.example.weather_app.webservices.model.current_weather_data.CurrentWeatherDataResponse
import com.example.weather_app.webservices.model.weather_forecast_data.WeatherForecastDataResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.util.*
import javax.inject.Inject


private const val TAG = "WeatherForecastVM"
@HiltViewModel
class WeatherForecastActivityViewModel @Inject constructor(
    private val repository: RepositoryImpl,
    private val apiKey: String
) : ViewModel() {

    val currentWeatherData: MutableLiveData<CurrentWeatherDataResponse> by lazy {
        MutableLiveData<CurrentWeatherDataResponse>()
    }

    val weatherForecastData: MutableLiveData<WeatherForecastDataResponse> by lazy {
        MutableLiveData<WeatherForecastDataResponse>()
    }

    private val weatherForecastLocationObserver = Observer<String> {
        viewModelScope.launch launchWhenCreated@{
            downloadWeatherData()
        }
    }

    private val unitOfMeasurementObserver = Observer<UnitOfMeasurement> {
        viewModelScope.launch launchWhenCreated@{
            downloadWeatherData()
        }
    }

    init {
        viewModelScope.launch launchWhenCreated@{
            downloadWeatherData()
        }

        repository.weatherForecastLocation.observeForever(weatherForecastLocationObserver)
        repository.unitOfMeasurement.observeForever(unitOfMeasurementObserver)
    }

    private suspend fun downloadWeatherData(){
        viewModelScope.launch launchWhenCreated@{
            val currentWeatherDayResponse = try {
                repository.getCurrentWeatherDataResponse(
                    apiKey,
                    repository.weatherForecastLocation.value!!,
                    repository.unitOfMeasurement.value!!.value)
            }catch (e: IOException){
                return@launchWhenCreated

            } catch (e: HttpException){
                return@launchWhenCreated
            }
            if (currentWeatherDayResponse.isSuccessful && currentWeatherDayResponse.body() != null ) {
                currentWeatherData.value = currentWeatherDayResponse.body()

                val weatherForecastDataResponse = try {
                    repository.getWeatherForecastDataResponse(
                        currentWeatherData.value?.coord!!.lat,
                        currentWeatherData.value?.coord!!.lon,
                        "current,minutely,alerts",
                        apiKey,
                        repository.unitOfMeasurement.value!!.value
                    )
                }catch (e: IOException){
                    return@launchWhenCreated

                } catch (e: HttpException){
                    return@launchWhenCreated
                }
                if (weatherForecastDataResponse.isSuccessful && currentWeatherDayResponse.body() != null){
                    weatherForecastData.value = weatherForecastDataResponse.body()
                }
            }
        }
    }

    fun getHourlyForecastList(): List<HourlyForecastData>{
        val list: MutableList<HourlyForecastData> = mutableListOf()

        list.add(
            HourlyForecastData(
            "Now",
            currentWeatherData.value?.main?.temp!!.toInt(),
            getWeatherIcon(currentWeatherData.value!!.weather[0].icon)
            )
        )
        for (i in 1..24){
            list.add(
                HourlyForecastData(
                    ClockUtils.getTimeFromUnixTimestamp(
                        weatherForecastData.value!!.hourly[i].dt * 1000L,
                        false,
                        true
                    ),
                    weatherForecastData.value!!.hourly[i].temp.toInt(),
                    getWeatherIcon(weatherForecastData.value!!.hourly[i].weather[0].icon)
                )
            )
        }
        return list

    }

    fun getVerticalWeatherDataList(): VerticalWeatherData{
        val dailyForecastList: List<DailyForecastData> = getDailyWeatherForecastList()
        val currentWeatherDataList: List<CurrentWeatherData> = getCurrentWeatherDataList()

        return VerticalWeatherData(dailyForecastList,currentWeatherDataList)
    }

    private fun getDailyWeatherForecastList(): List<DailyForecastData>{
        val dailyForecastList: MutableList<DailyForecastData> = mutableListOf()
        for (i in 0..7){
            dailyForecastList.add(
                DailyForecastData(
                    ClockUtils.getDayFromUnixTimestamp(weatherForecastData.value!!.daily[i].dt),
                    weatherForecastData.value!!.daily[i].temp.max.toInt(),
                    weatherForecastData.value!!.daily[i].temp.min.toInt(),
                    getWeatherIcon(weatherForecastData.value!!.daily[i].weather[0].icon)
                )
            )
        }
        return dailyForecastList
    }

    private fun getCurrentWeatherDataList(): List<CurrentWeatherData>{
        return listOf(
            CurrentWeatherData("SUNRISE", ClockUtils.getTimeFromUnixTimestamp(currentWeatherData.value!!.sys.sunrise.toLong(), true, true)
                ,"SUNSET",ClockUtils.getTimeFromUnixTimestamp(currentWeatherData.value!!.sys.sunset.toLong(), true,true)),
            CurrentWeatherData("PRESSURE","${currentWeatherData.value!!.main.pressure} Pa",
                "HUMIDITY","${currentWeatherData.value!!.main.humidity}%"),
            CurrentWeatherData("Wind","${currentWeatherData.value!!.wind.speed} km/h",
                "FEELS LIKE","${currentWeatherData.value!!.main.feels_like.toInt()}°"),
            CurrentWeatherData("WIND DEG","${currentWeatherData.value!!.wind.deg} deg",
                "VISIBILITY","${currentWeatherData.value!!.visibility} m"),
        )
    }

    private fun getWeatherIcon(iconTag: String): Int{
        return when(iconTag){
            "01d" -> R.drawable.clear_sky_day
            "01n" -> R.drawable.clear_sky_night
            "02d" -> R.drawable.few_clouds_day
            "02n" -> R.drawable.few_clouds_night
            "03d", "03n" -> R.drawable.scattered_clouds
            "04d", "04n" -> R.drawable.broken_clouds
            "09d", "09n" -> R.drawable.shower_rain
            "10d" -> R.drawable.rain_day
            "10n" -> R.drawable.rain_night
            "11d", "11n" -> R.drawable.thunderstorm
            "13d", "13n" -> R.drawable.snow
            "50d", "50n" -> R.drawable.mist
            else -> R.drawable.ic_few_clouds_dark
        }
    }

}
