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

    val citySelectionList: MutableLiveData<MutableList<CityShortcutData>> by lazy {
        MutableLiveData<MutableList<CityShortcutData>>()
    }

    val isCitiesListUpdated: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>(false)
    }

    fun updateMainWeatherForecastLocation(newLocation: String){
        repository.weatherForecastLocation.value = newLocation
    }

    fun getCitySelectionList() {
        val citiesNameList = repository.citySelectionList.value!!
        val cityShortcutDataList = mutableListOf<CityShortcutData>()
        viewModelScope.launch launchWhenCreated@{
            for (city in citiesNameList){
                val currentWeatherDataResponse = try {
                    repository.getCurrentWeatherDataResponse(
                        apiKey,
                        city,
                        "metric")
                }catch (e: IOException){
                    return@launchWhenCreated

                } catch (e: HttpException){
                    return@launchWhenCreated
                }
                if (currentWeatherDataResponse.isSuccessful && currentWeatherDataResponse.body() != null ){
                    val utcTime = currentWeatherDataResponse.body()!!.dt-7200
                    val timeZone = currentWeatherDataResponse.body()!!.timezone
                    val temp = currentWeatherDataResponse.body()!!.main.temp.toInt()
                    val localTime = ClockUtils.getTimeFromUnixTimestamp(
                        ClockUtils.getLocalTime(utcTime, timeZone),
                        true,
                        false
                    )
                    cityShortcutDataList.add(
                        CityShortcutData(
                            city,
                            localTime,
                            temp
                        )
                    )
                }
            }
            citySelectionList.value = cityShortcutDataList
        }
    }

    fun addNewLocationToCitySelectionList(cityName: String) {
        repository.citySelectionList.value!!.add(cityName)
        getCitySelectionList()
    }

}
