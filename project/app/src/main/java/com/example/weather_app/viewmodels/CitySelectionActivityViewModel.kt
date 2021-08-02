package com.example.weather_app.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
) : ViewModel() {

    var isCityListLoaded = false

    val citySelectionList: MutableLiveData<MutableList<CityShortcut>> by lazy {
        MutableLiveData<MutableList<CityShortcut>>()
    }

    private val unitOfMeasurementObserver = Observer<UnitOfMeasurement> { _ ->
        viewModelScope.launch launchWhenCreated@{
            if (isCityListLoaded){
                getCitySelectionList()
            }
        }
    }

    private val allCityShortcutListObserver = Observer<List<CityShortcut>> {
        viewModelScope.launch launchWhenCreated@{
            getCitySelectionList()
            isCityListLoaded = true
        }
    }

    init {
        repository.allCityShortcutList.observeForever(allCityShortcutListObserver)
        repository.unitOfMeasurement.observeForever(unitOfMeasurementObserver)
    }

    fun updateMainWeatherForecastLocation(newLocation: String){
        repository.mainForecastLocation.value = newLocation
    }

    fun getCitySelectionList() {
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
                    repository.unitOfMeasurement.value!!.value)
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

    fun deleteCityShortCut(cityShortcut: CityShortcut){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteCityShortcutFromDatabase(cityShortcut)
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
                 cityName,
                 localTime,
                 currentWeatherDataResponse.body()!!.main.temp.toInt(),
                 currentWeatherDataResponse.body()!!.weather[0].icon
             )
         }
         return  null
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
