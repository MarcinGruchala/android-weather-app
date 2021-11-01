package com.example.weather_app.presentation.selection

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather_app.domain.UnitOfMeasurement
import com.example.weather_app.domain.RepositoryImpl
import com.example.weather_app.persistence.entities.CityShortcut
import com.example.weather_app.presentation.common.ClockUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CitySelectionActivityViewModel @Inject constructor(
    private val repository: RepositoryImpl,
    private val apiKey: String,
    private val unitOfMeasurementSPEditor: SharedPreferences.Editor
) : ViewModel() {

    val citySelectionList: MutableLiveData<MutableList<CityShortcut>> by lazy {
        MutableLiveData<MutableList<CityShortcut>>()
    }

    val errorStatus: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>(false)
    }

    private var isCityListLoaded = false

    private val unitOfMeasurementObserver = Observer<String> {
        viewModelScope.launch {
            if (isCityListLoaded){
                updateCitySelectionList()
            }
        }
    }
    private val deviceLocationObserver = Observer<String> {
        viewModelScope.launch {
            if (repository.deviceLocation.value != null){
                updateCitySelectionList()
                isCityListLoaded = true
            }
        }
    }
    private val allCityShortcutListObserver = Observer<List<CityShortcut>> {
        viewModelScope.launch {
            if (repository.deviceLocation.value != null){
                updateCitySelectionList()
                isCityListLoaded = true
            }
        }
    }

    init {
        repository.deviceLocation.observeForever(deviceLocationObserver)
        repository.allCityShortcutList.observeForever(allCityShortcutListObserver)
        repository.unitOfMeasurement.observeForever(unitOfMeasurementObserver)
    }

    private fun updateCitySelectionList() {
        val citiesList = repository.allCityShortcutList.value
        val cityShortcutList = mutableListOf<CityShortcut>()
        viewModelScope.launch  {
            if (citiesList != null) {
                for (city in citiesList) {
                    val currentWeatherDataResponse = repository.getCurrentWeatherDataResponse(
                        apiKey,
                        city.cityName,
                        repository.unitOfMeasurement.value!!
                    )
                    if (currentWeatherDataResponse.isSuccessful &&
                        currentWeatherDataResponse.body() != null ) {
                        cityShortcutList.add (
                            CityShortcut(
                                city.id,
                                city.cityName,
                                ClockUtils.getTimeFromUnixTimestamp(
                                    System.currentTimeMillis(),
                                    currentWeatherDataResponse.body()!!.timezone * 1000L,
                                    repository.deviceTimezone * 1000L,
                                    true,
                                    clockPeriodMode = false
                                ),
                                currentWeatherDataResponse.body()!!.main.temp.toInt(),
                                currentWeatherDataResponse.body()!!.weather[0].icon
                            )
                        )
                    }
                }
            }
            val currentWeatherDayResponse = repository.getCurrentWeatherDataResponse(
                apiKey,
                repository.deviceLocation.value!!,
                repository.unitOfMeasurement.value!!
            )
            if (currentWeatherDayResponse.isSuccessful &&
                currentWeatherDayResponse.body() != null ) {
                cityShortcutList.add(
                    CityShortcut(
                        1000,
                        repository.deviceLocation.value!!,
                        "",
                        currentWeatherDayResponse.body()!!.main.temp.toInt(),
                        currentWeatherDayResponse.body()!!.weather[0].icon
                    )
                )
                citySelectionList.value = cityShortcutList
            }
        }
    }

    fun updateMainWeatherForecastLocation(
        newLocation: String
    ) {
        repository.mainForecastLocation.value = newLocation
    }

    fun addNewCityShortCutClickListener(
        cityName: String
    ) {
        viewModelScope.launch(Dispatchers.Main) {
            val cityShortcutData = getCityShortcutData(cityName)
            if (cityShortcutData!=null && !isInCitySelectionList(cityShortcutData.cityName)) {
                if (repository.deviceLocation.value == null) {
                    repository.deviceLocation.value = cityShortcutData.cityName
                    repository.mainForecastLocation.value = cityShortcutData.cityName
                } else {
                    insertCityShortcut(
                        CityShortcut(
                            0,
                            cityShortcutData.cityName,
                            cityShortcutData.localTime,
                            cityShortcutData.temp,
                            cityShortcutData.icon
                        )
                    )
                }
            }
        }
    }

    private fun isInCitySelectionList(
        cityName: String
    ): Boolean {
        for(city in citySelectionList.value!!) {
            if (city.cityName == cityName) {
                return true
            }
        }
        return false
    }

    private suspend fun getCityShortcutData(
        cityName: String
    ): com.example.weather_app.domain.CityShortcutData? {
        val currentWeatherDataResponse = repository.getCurrentWeatherDataResponse(
            apiKey,
            cityName,
            repository.unitOfMeasurement.value!!
        )
        if ( currentWeatherDataResponse.isSuccessful && currentWeatherDataResponse.body() != null ) {
            return com.example.weather_app.domain.CityShortcutData(
              currentWeatherDataResponse.body()!!.name,
              ClockUtils.getTimeFromUnixTimestamp(
                System.currentTimeMillis(),
                currentWeatherDataResponse.body()!!.timezone * 1000L,
                repository.deviceTimezone * 1000L,
                true,
                clockPeriodMode = false
              ),
              currentWeatherDataResponse.body()!!.main.temp.toInt(),
              currentWeatherDataResponse.body()!!.weather[0].icon
            )
        }
        errorStatus.value = true
        return  null
    }

    private fun insertCityShortcut(
        cityShortcut: CityShortcut
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addCityShortcutToDatabase(cityShortcut)
        }
    }

    fun deleteCityShortCutClickListener(
        cityShortcut: CityShortcut
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteCityShortcutFromDatabase(cityShortcut)
        }
    }

    fun changeUnitClickListener(): String {
        if (repository.unitOfMeasurement.value == UnitOfMeasurement.METRIC.value) {
            repository.unitOfMeasurement.value = UnitOfMeasurement.IMPERIAL.value
        } else {
            repository.unitOfMeasurement.value = UnitOfMeasurement.METRIC.value
        }
        unitOfMeasurementSPEditor.apply {
            putString("unitOfMeasurement", repository.unitOfMeasurement.value)
            apply()
        }
        return repository.unitOfMeasurement.value!!
    }

    fun getUnitMode() = repository.unitOfMeasurement.value!!
}
