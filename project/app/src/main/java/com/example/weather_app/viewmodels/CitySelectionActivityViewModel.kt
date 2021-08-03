package com.example.weather_app.viewmodels

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import com.example.weather_app.models.CityShortcutData
import com.example.weather_app.models.UnitOfMeasurement
import com.example.weather_app.models.entities.CityShortcut
import com.example.weather_app.repository.RepositoryImpl
import com.example.weather_app.utils.ClockUtils
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
    private val unitOfMeasurementSPEditor: SharedPreferences.Editor
) : ViewModel() {

    private var isCityListLoaded = false

    val citySelectionList: MutableLiveData<MutableList<CityShortcut>> by lazy {
        MutableLiveData<MutableList<CityShortcut>>()
    }

    val errorStatus: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>(false)
    }

    private val unitOfMeasurementObserver = Observer<String> { _ ->
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
        Log.d(TAG,"Get city selection list: ${repository.allCityShortcutList.value}")
        val citiesList = repository.allCityShortcutList.value
        val cityShortcutList = mutableListOf<CityShortcut>()
        viewModelScope.launch launchWhenCreated@{
            if (citiesList != null) {
                for (city in citiesList){
                    val currentWeatherDataResponse = try {
                        repository.getCurrentWeatherDataResponse(
                            apiKey,
                            city.cityName,
                            repository.unitOfMeasurement.value!!
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
                            repository.deviceTimezone * 1000L,
                            true,
                            false
                        )
                        cityShortcutList.add (
                            CityShortcut(
                                city.id,
                                city.cityName,
                                localTime,
                                temp,
                                currentWeatherDataResponse.body()!!.weather[0].icon
                            )
                        )
                    }
                }
            }
            val currentWeatherDayResponse = try {
                repository.getCurrentWeatherDataResponse(
                    apiKey,
                    repository.deviceLocation.value!!,
                    repository.unitOfMeasurement.value!!)
            }catch (e: IOException){
                return@launchWhenCreated

            } catch (e: HttpException){
                return@launchWhenCreated
            }
            if (currentWeatherDayResponse.isSuccessful && currentWeatherDayResponse.body() != null ) {
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

    fun updateMainWeatherForecastLocation(newLocation: String){
        repository.mainForecastLocation.value = newLocation
    }


    fun addNewCityShortCutClickListener(cityName: String){
        viewModelScope.launch(Dispatchers.Main) {
            val cityShortcutData = getCityShortcutData(cityName)
            if (cityShortcutData!=null){
                if (repository.deviceLocation.value == null){
                    repository.deviceLocation.value = cityShortcutData.cityName
                    repository.mainForecastLocation.value = cityShortcutData.cityName
                }
                else{
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
    }

    private suspend fun getCityShortcutData(cityName: String): CityShortcutData? {
        val currentWeatherDataResponse = repository.getCurrentWeatherDataResponse(
            apiKey,
            cityName,
            repository.unitOfMeasurement.value!!
        )
        if ( currentWeatherDataResponse.isSuccessful && currentWeatherDataResponse.body() != null ) {
            val utcTime = System.currentTimeMillis()
            val timeZone = currentWeatherDataResponse.body()!!.timezone
            val localTime = ClockUtils.getTimeFromUnixTimestamp(
                utcTime,
                timeZone*1000L,
                repository.deviceTimezone * 1000L,
                true,
                false
            )
            return CityShortcutData(
                currentWeatherDataResponse.body()!!.name,
                localTime,
                currentWeatherDataResponse.body()!!.main.temp.toInt(),
                currentWeatherDataResponse.body()!!.weather[0].icon
            )
        }
        errorStatus.value = true
        return  null
    }

    fun deleteCityShortCutClickListener(cityShortcut: CityShortcut){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteCityShortcutFromDatabase(cityShortcut)
        }
    }

    private fun insertCityShortcut(cityShortcut: CityShortcut){
        viewModelScope.launch(Dispatchers.IO) {
            repository.addCityShortcutToDatabase(cityShortcut)
        }
    }

    fun getUnitMode() = repository.unitOfMeasurement.value!!

    fun changeUnitClickListener(): String{
        if (repository.unitOfMeasurement.value == UnitOfMeasurement.METRIC.value) {
            repository.unitOfMeasurement.value = UnitOfMeasurement.IMPERIAL.value
        }
        else{
            repository.unitOfMeasurement.value = UnitOfMeasurement.METRIC.value
        }
        unitOfMeasurementSPEditor.apply {
            putString("unitOfMeasurement", repository.unitOfMeasurement.value)
            apply()
        }
        return repository.unitOfMeasurement.value!!
    }

}
