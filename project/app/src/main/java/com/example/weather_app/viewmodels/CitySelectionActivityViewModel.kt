package com.example.weather_app.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather_app.di.WeatherApplication
import com.example.weather_app.models.CityShortcutData
import com.example.weather_app.models.UnitOfMeasurement
import com.example.weather_app.repository.RepositoryImpl
import com.example.weather_app.utils.ClockUtils
import com.example.weather_app.utils.UiUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.util.*
import javax.inject.Inject

private const val TAG = "CitySelectionViewModel"
@HiltViewModel
class CitySelectionActivityViewModel @Inject constructor(
    private val repository: RepositoryImpl,
    private val apiKey: String,
    applicationContext: WeatherApplication
) : ViewModel() {

    private val cityListSharedPreferences =applicationContext.getSharedPreferences("citySelectionList", Context.MODE_PRIVATE)
    private val cityListSharedPreferencesEditor = cityListSharedPreferences.edit()

    val citySelectionList: MutableLiveData<MutableList<CityShortcutData>> by lazy {
        MutableLiveData<MutableList<CityShortcutData>>()
    }

    val isCitiesListUpdated: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>(false)
    }

    private val unitOfMeasurementObserver = Observer<UnitOfMeasurement> { _ ->
        viewModelScope.launch launchWhenCreated@{
            getCitySelectionList()
        }
    }

    init {
        repository.unitOfMeasurement.observeForever(unitOfMeasurementObserver)
    }

    fun updateMainWeatherForecastLocation(newLocation: String){
        repository.weatherForecastLocation.value = newLocation
    }

    fun getCitySelectionList() {
        val citiesNameList = loadCitySelectionListFromSharedPref()
        Log.d(TAG, "Updated List: $citiesNameList")
        val cityShortcutDataList = mutableListOf<CityShortcutData>()
        viewModelScope.launch launchWhenCreated@{
            for (city in citiesNameList){
                val currentWeatherDataResponse = try {
                    repository.getCurrentWeatherDataResponse(
                        apiKey,
                        city,
                        repository.unitOfMeasurement.value!!.value
                    )
                }catch (e: IOException){
                    return@launchWhenCreated

                } catch (e: HttpException){
                    return@launchWhenCreated
                }
                if (currentWeatherDataResponse.isSuccessful && currentWeatherDataResponse.body() != null ){
                    Log.d(TAG, "Current time: ${System.currentTimeMillis()}")
                    val deviceTimeZone = TimeZone.getDefault()
                    Log.d(TAG, "Device time zone: $deviceTimeZone")
                    val utcTime = System.currentTimeMillis()
                    val timeZone = currentWeatherDataResponse.body()!!.timezone
                    val temp = currentWeatherDataResponse.body()!!.main.temp.toInt()
                    val localTime = ClockUtils.getTimeFromUnixTimestamp(
                        utcTime,
                        timeZone*1000L,
                        true,
                        false
                    )
                    cityShortcutDataList.add(
                        CityShortcutData(
                            city,
                            localTime,
                            temp,
                            UiUtils.getWeatherIcon(
                                currentWeatherDataResponse.body()!!.weather[0].icon
                            )
                        )
                    )
                }
            }
            citySelectionList.value = cityShortcutDataList
        }
    }

    fun addNewLocationToCitySelectionList(cityName: String) {
        addNewLocationToCitySelectionListSharedPref(cityName)
        getCitySelectionList()
    }

    private fun loadCitySelectionListFromSharedPref(): List<String>{
        val citiesList = cityListSharedPreferences.getStringSet(
            "citySelectionList", setOf("Bydgoszcz")
        )
        if (citiesList != null) {
            return citiesList.toList()
        }
        return mutableListOf("Bydgoszcz")
    }

    private fun addNewLocationToCitySelectionListSharedPref(cityName: String){
        val citiesSet = cityListSharedPreferences.getStringSet(
            "citySelectionList",
            setOf("Bydgoszcz")
        )
        Log.d(TAG, "Before add: $citiesSet")
        val newCitiesSet: MutableSet<String> = citiesSet!!.toMutableSet()
        newCitiesSet.add(cityName)
        Log.d(TAG, "After add: $newCitiesSet")
        cityListSharedPreferencesEditor.apply {
            putStringSet("citySelectionList", newCitiesSet)
            apply()
        }
    }

    fun getUnitMode() = repository.unitOfMeasurement.value!!

    fun changeUnit(): UnitOfMeasurement{
        if (repository.unitOfMeasurement.value == UnitOfMeasurement.METRIC) {
            repository.unitOfMeasurement.value = UnitOfMeasurement.IMPERIAL
            return UnitOfMeasurement.IMPERIAL
        }
        repository.unitOfMeasurement.value = UnitOfMeasurement.METRIC
        return UnitOfMeasurement.METRIC
    }

}
