package com.example.weather_app.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weather_app.adapters.HourlyForecastAdapter
import com.example.weather_app.adapters.VerticalWeatherDataAdapter
import com.example.weather_app.data.CurrentWeatherData
import com.example.weather_app.data.DailyForecastData
import com.example.weather_app.data.HourlyForecastData
import com.example.weather_app.data.VerticalWeatherData
import com.example.weather_app.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val hourlyForecastList = listOf(
            HourlyForecastData("Now",20),
            HourlyForecastData("10AM",20),
            HourlyForecastData("11AM",20),
            HourlyForecastData("12PM",21),
            HourlyForecastData("13PM",21),
            HourlyForecastData("14PM",21),
            HourlyForecastData("15PM",22),
            HourlyForecastData("16PM",22),
            HourlyForecastData("17PM",22),
            HourlyForecastData("18PM",22),
            HourlyForecastData("19PM",21),
            HourlyForecastData("20PM",21),
            HourlyForecastData("21PM",20),
            HourlyForecastData( "22PM",20),
            HourlyForecastData("23PM",19),
            HourlyForecastData("24AM",19),
            HourlyForecastData("1AM",19),
            HourlyForecastData("2AM",18),
            HourlyForecastData("3AM",18),
            HourlyForecastData("4AM",18),
            HourlyForecastData("5AM",17),
            HourlyForecastData("6AM",18),
            HourlyForecastData("7AM",19),
            HourlyForecastData("8AM",20),
            HourlyForecastData("9AM",20),
        )

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


        binding.rvHourlyForecast.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        binding.rvHourlyForecast.adapter = HourlyForecastAdapter(hourlyForecastList)

        binding.rvVerticalWeatherData.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvVerticalWeatherData.adapter = VerticalWeatherDataAdapter(verticalWeatherData)


    }
}