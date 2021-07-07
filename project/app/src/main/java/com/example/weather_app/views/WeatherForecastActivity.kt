package com.example.weather_app.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weather_app.R
import com.example.weather_app.adapters.HourlyForecastAdapter
import com.example.weather_app.adapters.VerticalWeatherDataAdapter
import com.example.weather_app.databinding.ActivityWeatherForecastBinding
import com.example.weather_app.viewmodels.MainActivityViewModel
import com.example.weather_app.webservices.model.current_weather_data.CurrentWeatherDataResponse
import com.example.weather_app.webservices.model.weather_forecast_data.WeatherForecastDataResponse
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class WeatherForecastActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWeatherForecastBinding
    private val viewModel: MainActivityViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWeatherForecastBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recyclerViewsSetup()
        setUpClickListeners()

        val currentWeatherDataObserver = Observer<CurrentWeatherDataResponse> { newData ->
            updateTopScreenUI(newData)
        }
        viewModel.currentWeatherData.observe(this,currentWeatherDataObserver)

        val weatherForecastDataObserver = Observer<WeatherForecastDataResponse> { _ ->
            updateRecyclerViews()
        }
        viewModel.weatherForecastData.observe(this,weatherForecastDataObserver)

    }

    private fun setUpClickListeners(){
        binding.btnCitySelection.setOnClickListener {
            Intent(this,CitySelectionActivity::class.java).also {
                startActivity(it)
            }
        }
    }

    private fun recyclerViewsSetup(){
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

    private fun updateTopScreenUI(data: CurrentWeatherDataResponse){
        binding.tvCity.text = data.name
        binding.tvWeatherDescription.text = data.weather[0].description.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(
                Locale.getDefault()
            ) else it.toString()
        }
        binding.tvTemp.text = getString(
            R.string.main_temp,
            data.main.temp.toInt()
        )
        binding.tvH.text = getString(
            R.string.main_H_temp,
            data.main.temp_max.toInt()
        )
        binding.tvL.text = getString(
            R.string.main_L_temp,
            data.main.temp_min.toInt()
        )
    }

    private fun  updateRecyclerViews(){
        binding.rvHourlyForecast.adapter = HourlyForecastAdapter(viewModel.getHourlyForecastList())
        binding.rvVerticalWeatherData.adapter = VerticalWeatherDataAdapter(viewModel.getVerticalWeatherDataList())
    }
}
