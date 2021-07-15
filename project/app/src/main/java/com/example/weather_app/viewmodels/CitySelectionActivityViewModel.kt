package com.example.weather_app.viewmodels

import androidx.lifecycle.ViewModel
import com.example.weather_app.repository.RepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CitySelectionActivityViewModel @Inject constructor(
    private val repository: RepositoryImpl
) : ViewModel() {

    fun updateMainWeatherForecastLocation(newLocation: String){
        repository.weatherForecastLocation.value = newLocation
    }

}