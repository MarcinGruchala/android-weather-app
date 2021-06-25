package com.example.weather_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weather_app.databinding.ActivityMainBinding

private lateinit var binding: ActivityMainBinding
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var hourlyForecastList = listOf(
            HourlyForecast("Now",20),
            HourlyForecast("10AM",20),
            HourlyForecast("11AM",20),
            HourlyForecast("12PM",21),
            HourlyForecast("13PM",21),
            HourlyForecast("14PM",21),
            HourlyForecast("15PM",22),
            HourlyForecast("16PM",22),
            HourlyForecast("17PM",22),
            HourlyForecast("18PM",22),
            HourlyForecast("19PM",21),
            HourlyForecast("20PM",21),
            HourlyForecast("21PM",20),
            HourlyForecast( "22PM",20),
            HourlyForecast("23PM",19),
            HourlyForecast("24AM",19),
            HourlyForecast("1AM",19),
            HourlyForecast("2AM",18),
            HourlyForecast("3AM",18),
            HourlyForecast("4AM",18),
            HourlyForecast("5AM",17),
            HourlyForecast("6AM",18),
            HourlyForecast("7AM",19),
            HourlyForecast("8AM",20),
            HourlyForecast("9AM",20),
        )

        var dailyForecastList = listOf(
            DailyForecast("Friday",28,15),
            DailyForecast("Saturday", 34,21),
            DailyForecast("Sunday",29,14),
            DailyForecast("Monday",24,12),
            DailyForecast("Tuesday",19,13),
            DailyForecast("Wednesday",23,16)
        )

        val currentWeatherDataList = listOf(
            CurrentWeatherData("SUNRISE","6:42 AM","SUNSET","8:49PM"),
            CurrentWeatherData("CHANCE OF RAIN","30%","HUMIDITY","69%")

        )


        binding.rvHourlyForecast.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        binding.rvHourlyForecast.adapter = HourlyForecastAdapter(hourlyForecastList)

        binding.rvDailyForecast.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        binding.rvDailyForecast.adapter = DailyForecastAdapter(dailyForecastList)

        binding.rvCurrentWeatherData.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        binding.rvCurrentWeatherData.adapter = CurrentWeatherDataAdapter(currentWeatherDataList)
    }
}