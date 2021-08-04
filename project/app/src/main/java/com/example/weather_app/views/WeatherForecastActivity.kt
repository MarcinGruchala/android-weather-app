package com.example.weather_app.views

import android.annotation.SuppressLint
import android.content.Intent
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weather_app.R
import com.example.weather_app.adapters.HourlyForecastAdapter
import com.example.weather_app.adapters.VerticalWeatherDataAdapter
import com.example.weather_app.databinding.ActivityWeatherForecastBinding
import com.example.weather_app.utils.UiUtils
import com.example.weather_app.viewmodels.WeatherForecastActivityViewModel
import com.example.weather_app.webservices.model.current_weather_data.CurrentWeatherDataResponse
import com.example.weather_app.webservices.model.weather_forecast_data.WeatherForecastDataResponse
import com.google.android.gms.location.LocationServices
import com.vmadalin.easypermissions.EasyPermissions
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

private const val TAG = "WeatherForecastActivity"
private const val PERMISSION_LOCATION_REQUEST_CODE = 10
@AndroidEntryPoint
class WeatherForecastActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks {
    private lateinit var binding: ActivityWeatherForecastBinding
    private val viewModel: WeatherForecastActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWeatherForecastBinding.inflate(layoutInflater)
        setContentView(binding.root)
        checkPermissions()
        getDeviceLocation()

        setUpRecyclerViews()
        setUpClickListeners()

        val currentWeatherDataObserver = Observer<CurrentWeatherDataResponse> { newData ->
            updateBackground()
            updateTextViews(newData)
        }
        viewModel.currentWeatherData.observe(this,currentWeatherDataObserver)

        val weatherForecastDataObserver = Observer<WeatherForecastDataResponse> {
            updateRecyclerViews()
        }
        viewModel.weatherForecastData.observe(this,weatherForecastDataObserver)

        val errorStatusObserver = Observer<Boolean> { status ->
            if (status){
                viewModel.errorStatus.value = false
                showInsertLocalityDialog(
                    applicationContext.getString(
                        R.string.didnt_found_city
                    )
                )
            }
        }
        viewModel.errorStatus.observe(this,errorStatusObserver)

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions,grantResults,this)
    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        showInsertLocalityDialog(
            applicationContext.getString(
                R.string.no_permission_dialog_message
            )
        )
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        Toast.makeText(
            this,
            "Location permission granted.",
            Toast.LENGTH_SHORT
        ).show()
        getDeviceLocation()
    }

    private fun setUpClickListeners(){
        binding.btnCitySelection.setOnClickListener {
            Intent(this,CitySelectionActivity::class.java).also {
                startActivity(it)
            }
        }
    }

    private fun setUpRecyclerViews(){
        binding.rvHourlyForecast.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.HORIZONTAL,
            false
        )

        binding.rvVerticalWeatherData.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            false
        )
    }

    private fun updateBackground(){
        val weatherTag = viewModel.currentWeatherData.value!!.weather[0].icon
        binding.root.setBackgroundResource(
            UiUtils.getWeatherForecastBackground(
                weatherTag
            )
        )
        window.statusBarColor = ContextCompat.getColor(
            this,
            UiUtils.getStatusBarColor(weatherTag)
        )

        binding.dividerTopHourlyForecast.setBackgroundResource(
            UiUtils.getHeaderColor(weatherTag)
        )
        binding.dividerBottomHourlyForecast.setBackgroundResource(
            UiUtils.getHeaderColor(weatherTag)
        )

        binding.divider.setBackgroundResource(
            UiUtils.getHeaderColor(weatherTag)
        )
    }

    private fun updateTextViews(data: CurrentWeatherDataResponse){
        binding.apply {
            tvCity.textSize = 40f
            tvCity.text = data.name
            tvWeatherDescription.text = data.weather[0].description.replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(
                    Locale.getDefault()
                ) else it.toString()
            }
            tvTemp.text = getString(
                R.string.main_temp,
                data.main.temp.toInt()
            )
            tvH.text = getString(
                R.string.main_H_temp,
                data.main.temp_max.toInt()
            )
            tvL.text = getString(
                R.string.main_L_temp,
                data.main.temp_min.toInt()
            )
            tvApiCallTime.setTextColor(
                ContextCompat.getColor(
                    applicationContext,
                    UiUtils.getHeaderColor(viewModel.currentWeatherData.value!!.weather[0].icon
                    )
                )
            )
            tvApiCallTime.text = viewModel.getApiCallTime()
        }


    }

    private fun  updateRecyclerViews(){
        binding.rvHourlyForecast.adapter = HourlyForecastAdapter(viewModel.getHourlyForecastList())
        binding.rvVerticalWeatherData.adapter = VerticalWeatherDataAdapter(
            viewModel.currentWeatherData.value!!.weather[0].icon,
            viewModel.getVerticalWeatherDataList()
        )
    }

    private fun checkPermissions(){
        if (!hasLocationPermission()){
            requestLocationPermission()
        }
    }

    private fun requestLocationPermission(){
        EasyPermissions.requestPermissions(
            this,
            "This application works best with location permission",
            PERMISSION_LOCATION_REQUEST_CODE,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        )
    }

    @SuppressLint("MissingPermission")
    private fun getDeviceLocation(){
        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        if (hasLocationPermission()){
            fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
                Log.d(TAG,"Location founded")
                if (location == null){
                    Log.d(TAG,"Location null")
                    showInsertLocalityDialog(
                        applicationContext.getString(
                            R.string.insert_location_dialog_description
                        )
                    )
                }
                else{
                    val lat = location.latitude
                    val lon = location.longitude
                    val locality = Geocoder(this).getFromLocation(
                        lat,
                        lon,
                        1
                    ).first().locality
                    Log.d(TAG,"Locality: $locality")
                    if (locality == null){
                        Log.d(TAG, "Didn't found the locality")
                        showInsertLocalityDialog(
                            applicationContext.getString(
                                R.string.insert_location_dialog_description
                            )
                        )
                    }
                    else{
                        viewModel.updateDeviceLocation(locality)
                    }
                }
            }
        }
    }

    private fun hasLocationPermission() = EasyPermissions.hasPermissions(
        this,
        android.Manifest.permission.ACCESS_FINE_LOCATION
    )

    private fun showInsertLocalityDialog(dialogMessage: String){
        val dialog = InsertLocationDialog(
            dialogMessage,
            btnSubmitClickListener = { locality ->
                viewModel.updateDeviceLocation(locality)
            }
        )
        dialog.show(supportFragmentManager,"insertLocationDialog")
    }

    private fun showErrorDialogWindow() {
        AlertDialog.Builder(this)
            .setTitle("Error message")
            .setMessage(
                applicationContext.getString(
                    R.string.didnt_found_city
                )
            )
            .setPositiveButton("OK") { _, _ -> }
            .create()
            .show()
    }

}
