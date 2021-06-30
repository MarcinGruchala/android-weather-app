package com.example.weather_app.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather_app.BuildConfig
import com.example.weather_app.models.HourlyForecastData
import com.example.weather_app.webservices.OpenWeatherAPIClient
import com.example.weather_app.webservices.model.current_weather_data.CurrentWeatherDataResponse
import com.example.weather_app.webservices.model.weather_forecast_data.Hourly
import com.example.weather_app.webservices.model.weather_forecast_data.WeatherForecastDataResponse
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.sql.Timestamp
import java.text.SimpleDateFormat
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
                    convertUnixTimestamp(weatherForecastData.value!!.hourly[i].dt),
                    weatherForecastData.value!!.hourly[i].temp.toInt()
                )
            )
        }
        return list

    }

    private fun  convertUnixTimestamp(unixTimeStamp: Int): String{
        return when(val hour = Timestamp(unixTimeStamp.toLong()*1000L).hours){
            in 0..12 -> "$hour AM"
            in 13..24 -> "${hour-12} PM"
            else -> hour.toString()
        }
    }


}
