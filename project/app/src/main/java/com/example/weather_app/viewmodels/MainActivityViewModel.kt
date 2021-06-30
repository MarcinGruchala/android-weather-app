package com.example.weather_app.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather_app.BuildConfig
import com.example.weather_app.webservices.OpenWeatherAPIClient
import com.example.weather_app.webservices.model.CurrentWeatherDataResponse
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class MainActivityViewModel : ViewModel() {
    val currentWeatherData: MutableLiveData<CurrentWeatherDataResponse> by lazy {
        MutableLiveData<CurrentWeatherDataResponse>()
    }

    init {
        viewModelScope.launch launchWhenCreated@{
            val response = try {
                OpenWeatherAPIClient.api.getCurrentWeatherData("Bydgoszcz", BuildConfig.APIKEY,"metric")
            }catch (e: IOException){
                return@launchWhenCreated

            } catch (e: HttpException){
                return@launchWhenCreated
            }
            if (response.isSuccessful && response.body() != null ) {
                currentWeatherData.value = response.body()
            }
        }
    }
}