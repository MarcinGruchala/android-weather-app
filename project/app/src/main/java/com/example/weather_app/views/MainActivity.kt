package com.example.weather_app.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weather_app.R
import com.example.weather_app.adapters.HourlyForecastAdapter
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
    val viewModel: MainActivityViewModel by viewModels()
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

        val dailyForecastList = listOf(
            DailyForecastData("Friday",28,15),
            DailyForecastData("Saturday", 34,21),
            DailyForecastData("Sunday",29,14),
            DailyForecastData("Monday",24,12),
            DailyForecastData("Tuesday",19,13),
            DailyForecastData("Wednesday",23,16),
            DailyForecastData("Thursday",22,17),
            DailyForecastData("Friday",21,16),
            DailyForecastData("Sunday",29,14),
            DailyForecastData("Monday",24,12)
            )

        val currentWeatherDataList = listOf(
            CurrentWeatherData("SUNRISE","6:42 AM","SUNSET","8:49PM"),
            CurrentWeatherData("CHANCE OF RAIN","30%","HUMIDITY","69%"),
            CurrentWeatherData("Wind","NNW 4 mph", "FEELS LIKE","77"),
            CurrentWeatherData("PRECIPITATION", "0 in","PRESSURE","29.8 inHg"),
            CurrentWeatherData("VISIBILITY","10 mi","UV INDEX","0"),
            CurrentWeatherData("AIR QUALITY INDEX", "38","AIR QUALITY", "Good")
        )

        val verticalWeatherData = VerticalWeatherData(dailyForecastList,currentWeatherDataList)


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
        //binding.rvVerticalWeatherData.adapter = VerticalWeatherDataAdapter(verticalWeatherData)

    }
}
