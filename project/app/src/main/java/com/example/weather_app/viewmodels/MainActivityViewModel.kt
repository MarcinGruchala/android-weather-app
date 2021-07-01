package com.example.weather_app.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather_app.BuildConfig
import com.example.weather_app.models.CurrentWeatherData
import com.example.weather_app.models.DailyForecastData
import com.example.weather_app.models.HourlyForecastData
import com.example.weather_app.models.VerticalWeatherData
import com.example.weather_app.webservices.OpenWeatherAPIClient
import com.example.weather_app.webservices.model.current_weather_data.CurrentWeatherDataResponse
import com.example.weather_app.webservices.model.weather_forecast_data.WeatherForecastDataResponse
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.sql.Timestamp
import java.util.*

class MainActivityViewModel : ViewModel() {
    val currentWeatherData: MutableLiveData<CurrentWeatherDataResponse> by lazy {
        MutableLiveData<CurrentWeatherDataResponse>()
    }

    val weatherForecastData: MutableLiveData<WeatherForecastDataResponse> by lazy {
        MutableLiveData<WeatherForecastDataResponse>()
    }

    init {
        viewModelScope.launch launchWhenCreated@{
            val currentWeatherDayaResponse = try {
                OpenWeatherAPIClient.api.getCurrentWeatherData(
                    "Bydgoszcz",
                    BuildConfig.APIKEY,
                    "metric"
                )
            }catch (e: IOException){
                return@launchWhenCreated

            } catch (e: HttpException){
                return@launchWhenCreated
            }
            if (currentWeatherDayaResponse.isSuccessful && currentWeatherDayaResponse.body() != null ) {
                currentWeatherData.value = currentWeatherDayaResponse.body()

                val weatherForecastDataResponse = try {
                    OpenWeatherAPIClient.api.getWeatherForecastData(
                        currentWeatherData.value?.coord!!.lat,
                        currentWeatherData.value?.coord!!.lon,
                        "current,minutely,alerts",
                        BuildConfig.APIKEY,
                        "metric"
                    )
                }catch (e: IOException){
                    return@launchWhenCreated

                } catch (e: HttpException){
                    return@launchWhenCreated
                }
                if (weatherForecastDataResponse.isSuccessful && currentWeatherDayaResponse.body() != null){
                    weatherForecastData.value = weatherForecastDataResponse.body()
                }
            }
        }
    }

    fun getHourlyForecastList(): List<HourlyForecastData>{
        val list: MutableList<HourlyForecastData> = mutableListOf()

        list.add(HourlyForecastData("Now", currentWeatherData.value?.main?.temp!!.toInt()))
        for (i in 1..24){
            list.add(
                HourlyForecastData(
                    getHourFromUnixTimestamp(weatherForecastData.value!!.hourly[i].dt),
                    weatherForecastData.value!!.hourly[i].temp.toInt()
                )
            )
        }
        return list

    }

    private fun  getHourFromUnixTimestamp(unixTimeStamp: Int): String{
        Log.d("Hour", Timestamp(unixTimeStamp.toLong()*1000L).hours.toString())
        return when(val hour = Timestamp(unixTimeStamp.toLong()*1000L).hours){
            in 0..11 -> "$hour AM"
            12 -> "12 PM"
            in 13..23 -> "${hour-12} PM"
            24 -> "24 AM"
            else -> hour.toString()
        }
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
                    getDayFromUnixTimestamp(weatherForecastData.value!!.daily[i].dt),
                    weatherForecastData.value!!.daily[i].temp.max.toInt(),
                    weatherForecastData.value!!.daily[i].temp.min.toInt()
                )
            )
        }
        return dailyForecastList
    }

    private fun getDayFromUnixTimestamp(unixTimeStamp: Int): String{
        return when(val day = Timestamp(unixTimeStamp.toLong()*1000L).day){
            0 -> "Sunday"
            1 -> "Monday"
            2 -> "Tuesday"
            3 -> "Wednesday"
            4 -> "Thursday"
            5 -> "Friday"
            6 -> "Saturday"
            else -> "Day: $day"
        }
    }

    private fun getCurrentWeatherDataList(): List<CurrentWeatherData>{
        return listOf(
            CurrentWeatherData("SUNRISE", getTimeFromUnixTimestamp(currentWeatherData.value!!.sys.sunrise)
                ,"SUNSET", getTimeFromUnixTimestamp(currentWeatherData.value!!.sys.sunset)),
            CurrentWeatherData("PRESSURE","${currentWeatherData.value!!.main.pressure} Pa",
                "HUMIDITY","${currentWeatherData.value!!.main.humidity}%"),
            CurrentWeatherData("Wind","${currentWeatherData.value!!.wind.speed} km/h",
                "FEELS LIKE","${currentWeatherData.value!!.main.feels_like.toInt()}°"),
            CurrentWeatherData("WIND DEG","${currentWeatherData.value!!.wind.deg} deg",
                "VISIBILITY","${currentWeatherData.value!!.visibility} m"),
        )
    }

    private fun getTimeFromUnixTimestamp(unixTimeStamp: Int): String{
        return "${Timestamp(unixTimeStamp*1000L).hours}:${Timestamp(unixTimeStamp*1000L).minutes}"
    }
}
