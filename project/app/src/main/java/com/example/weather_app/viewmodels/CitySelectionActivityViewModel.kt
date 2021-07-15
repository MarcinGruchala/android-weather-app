package com.example.weather_app.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather_app.models.CityShortcutData
import com.example.weather_app.repository.RepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

private const val TAG = "CitySelectionActivityViewModel"
@HiltViewModel
class CitySelectionActivityViewModel @Inject constructor(
    private val repository: RepositoryImpl,
    private val apiKey: String
) : ViewModel() {

    val isCitiesListUpdated: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>(false)
    }

    fun updateMainWeatherForecastLocation(newLocation: String){
        repository.weatherForecastLocation.value = newLocation
    }

    fun getCitySelectionList() = repository.citySelectionList.value

    fun updateCitySelectionList(cityName: String) {
        viewModelScope.launch launchWhenCreated@{
            val currentWeatherDayResponse = try {
                repository.getCurrentWeatherDataResponse(
                    apiKey,
                    cityName,
                    "metric")
            }catch (e: IOException){
                return@launchWhenCreated

            } catch (e: HttpException){
                return@launchWhenCreated
            }
            if (currentWeatherDayResponse.isSuccessful && currentWeatherDayResponse.body() != null ){
                repository.citySelectionList.value!!.add(
                    CityShortcutData(
                        cityName,getLocalTime(),
                        currentWeatherDayResponse.body()!!.main.temp.
                        toInt()
                    )
                )
                isCitiesListUpdated.value = true
            }
        }
    }

    private fun getLocalTime(): String{
        return ""
    }

}