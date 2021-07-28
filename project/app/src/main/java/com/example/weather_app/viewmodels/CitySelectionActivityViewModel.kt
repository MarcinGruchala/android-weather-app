package com.example.weather_app.viewmodels

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather_app.di.WeatherApplication
import com.example.weather_app.models.CityShortcutData
import com.example.weather_app.models.UnitOfMeasurement
import com.example.weather_app.models.entities.CityShortcut
import com.example.weather_app.repository.RepositoryImpl
import com.example.weather_app.utils.ClockUtils
import com.example.weather_app.utils.UiUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
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

    private val unitOfMeasurementObserver = Observer<UnitOfMeasurement> { _ ->
        viewModelScope.launch launchWhenCreated@{
            getCitySelectionList()
        }
    }

    private val allCityShortcutListObserver = Observer<List<CityShortcut>> {
        Log.d(TAG,"City shortcut list: ${repository.allCityShortcutList.value}")
    }

    init {
        repository.unitOfMeasurement.observeForever(unitOfMeasurementObserver)
        repository.allCityShortcutList.observeForever(allCityShortcutListObserver)
    }

    fun updateMainWeatherForecastLocation(newLocation: String){
        repository.mainForecastLocation.value = newLocation
    }

    fun getCitySelectionList() {
        val citiesNameList = loadCitySelectionListFromSharedPref()
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
            val currentWeatherDayResponse = try {
                repository.getCurrentWeatherDataResponse(
                    apiKey,
                    repository.mainForecastLocation.value!!,
                    repository.unitOfMeasurement.value!!.value)
            }catch (e: IOException){
                return@launchWhenCreated

            } catch (e: HttpException){
                return@launchWhenCreated
            }
            if (currentWeatherDayResponse.isSuccessful && currentWeatherDayResponse.body() != null ) {
                cityShortcutDataList.add(
                    CityShortcutData(
                        repository.deviceLocation.value!!,
                        "",
                        currentWeatherDayResponse.body()!!.main.temp.toInt(),
                        UiUtils.getWeatherIcon(currentWeatherDayResponse.body()!!.weather[0].icon)
                    )
                )
                citySelectionList.value = cityShortcutDataList
            }
        }
    }

    fun addNewCityShortCut(cityName: String){
        viewModelScope.launch(Dispatchers.IO) {
            val cityShortcutData = getCityShortcutData(cityName)
            if (cityShortcutData!=null){
                insertCityShortcut(
                    CityShortcut(
                        0,
                        cityShortcutData.cityName,
                        cityShortcutData.localTime,
                        cityShortcutData.temp,
                        cityShortcutData.icon
                    )
                )
                Log.d(TAG, "Inserted Data to database")
            }
        }

    }

    private fun insertCityShortcut(cityShortcut: CityShortcut){
        viewModelScope.launch(Dispatchers.IO) {
            repository.addCityShortcutToDatabase(cityShortcut)
        }
    }

     suspend fun getCityShortcutData(cityName: String): CityShortcutData? {
         val currentWeatherDataResponse = repository.getCurrentWeatherDataResponse(
             apiKey,
             cityName,
             repository.unitOfMeasurement.value!!.value
         )
         if ( currentWeatherDataResponse.isSuccessful && currentWeatherDataResponse.body() != null ) {
             val deviceTimeZone = TimeZone.getDefault()
             val utcTime = System.currentTimeMillis()
             val timeZone = currentWeatherDataResponse.body()!!.timezone
             val localTime = ClockUtils.getTimeFromUnixTimestamp(
                 utcTime,
                 timeZone*1000L,
                 true,
                 false
             )
             return CityShortcutData(
                 cityName,
                 localTime,
                 currentWeatherDataResponse.body()!!.main.temp.toInt(),
                 UiUtils.getWeatherIcon(
                     currentWeatherDataResponse.body()!!.weather[0].icon
                 )
             )
         }
         return  null
    }

    fun addNewLocationToCitySelectionList(cityName: String) {
        addNewCityShortCut(cityName)
        addNewLocationToCitySelectionListSharedPref(cityName)
        getCitySelectionList()
    }

    private fun loadCitySelectionListFromSharedPref(): List<String>{
        val citiesList = cityListSharedPreferences.getStringSet(
            "citySelectionList", null
        )
        if (citiesList != null) {
            return citiesList.toList()
        }
        return mutableListOf()
    }

    private fun addNewLocationToCitySelectionListSharedPref(cityName: String){
        val citiesSet = cityListSharedPreferences.getStringSet(
            "citySelectionList",
            setOf()
        )
        val newCitiesSet: MutableSet<String> = citiesSet!!.toMutableSet()
        newCitiesSet.add(cityName)
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
