package com.example.weather_app.domain.forecast

import com.example.weather_app.domain.forecast.CurrentWeather
import com.example.weather_app.domain.forecast.DailyForecast

class VerticalWeather(
    val dailyForecastList: List<DailyForecast>,
    val currentWeatherList: List<CurrentWeather>
) {

    fun getSize(): Int = dailyForecastList.size + currentWeatherList.size
}
