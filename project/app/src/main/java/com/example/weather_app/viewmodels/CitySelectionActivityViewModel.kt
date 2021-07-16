package com.example.weather_app.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather_app.models.CityShortcutData
import com.example.weather_app.repository.RepositoryImpl
import com.example.weather_app.utils.ClockUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

private const val TAG = "CitySelectionViewModel"
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

    fun addNewLocationToCitySelectionList(cityName: String) {
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
                val utcTime = currentWeatherDayResponse.body()!!.dt-7200
                val timeZone = currentWeatherDayResponse.body()!!.timezone
                val temp = currentWeatherDayResponse.body()!!.main.temp.toInt()
                val localTime = ClockUtils.getTimeFromUnixTimestamp(
                    ClockUtils.getLocalTime(utcTime, timeZone),
                    true,
                    false
                )
                repository.citySelectionList.value!!.add(
                    CityShortcutData(
                        cityName,
                        localTime,
                        temp
                    )
                )
                isCitiesListUpdated.value = true
            }
        }
    }

    fun updateCityLocationList() {
        viewModelScope.launch launchWhenCreated@{
            for (city in repository.citySelectionList.value!!) {
                val currentWeatherDayResponse = try {
                    repository.getCurrentWeatherDataResponse(
                        apiKey,
                        city.cityName,
                        "metric"
                    )
                } catch (e: IOException) {
                    return@launchWhenCreated

                } catch (e: HttpException) {
                    return@launchWhenCreated
                }
                if (currentWeatherDayResponse.isSuccessful && currentWeatherDayResponse.body() != null) {
                    val utcTime = currentWeatherDayResponse.body()!!.dt-7200
                    val timeZone = currentWeatherDayResponse.body()!!.timezone
                    val temp = currentWeatherDayResponse.body()!!.main.temp.toInt()
                    val localTime = ClockUtils.getTimeFromUnixTimestamp(
                        ClockUtils.getLocalTime(utcTime, timeZone),
                        true,
                        false
                    )
                    city.localTime = localTime
                    city.temp = temp
                }
            }
        }
    }
}
