package com.example.weather_app.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weather_app.R
import com.example.weather_app.adapters.HourlyForecastAdapter
import com.example.weather_app.adapters.VerticalWeatherDataAdapter
import com.example.weather_app.models.CurrentWeatherData
import com.example.weather_app.models.DailyForecastData
import com.example.weather_app.models.HourlyForecastData
import com.example.weather_app.models.VerticalWeatherData
import com.example.weather_app.databinding.ActivityMainBinding
import com.example.weather_app.viewmodels.MainActivityViewModel
import com.example.weather_app.webservices.model.current_weather_data.CurrentWeatherDataResponse
import com.example.weather_app.webservices.model.weather_forecast_data.WeatherForecastDataResponse
import java.util.*


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainActivityViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recyclerViewsSetup()

        val currentWeatherDataObserver = Observer<CurrentWeatherDataResponse> { newData ->
            updateTopScreenUI(newData)
        }
        viewModel.currentWeatherData.observe(this,currentWeatherDataObserver)

        val weatherForecastDataObserver = Observer<WeatherForecastDataResponse> { _ ->
            updateRecyclerViews()
        }
        viewModel.weatherForecastData.observe(this,weatherForecastDataObserver)

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
