package com.example.weather_app.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather_app.BuildConfig
import com.example.weather_app.webservices.OpenWeatherAPIClient
import com.example.weather_app.webservices.model.current_weather_data.CurrentWeatherDataResponse
import com.example.weather_app.webservices.model.weather_forecast_data.WeatherForecastDataResponse
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

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
}
