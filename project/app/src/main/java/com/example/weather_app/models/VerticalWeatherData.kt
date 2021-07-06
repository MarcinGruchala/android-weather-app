package com.example.weather_app.models

class VerticalWeatherData(
    val dailyForecastList: List<DailyForecastData>,
    val currentWeatherDataList: List<CurrentWeatherData>
) {

    fun getSize(): Int = dailyForecastList.size + currentWeatherDataList.size

}
