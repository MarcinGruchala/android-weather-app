package com.example.weather_app

class VerticalWeatherData(
    val dailyForecastList: List<DailyForecast>,
    val currentWeatherDataList: List<CurrentWeatherData>
) {

    fun getSize(): Int = dailyForecastList.size + currentWeatherDataList.size

}
