package com.example.weather_app.data

class VerticalWeatherData(
    val dailyForecastList: List<DailyForecastData>,
    val currentWeatherDataList: List<CurrentWeatherData>
) {

    fun getSize(): Int = dailyForecastList.size + currentWeatherDataList.size

}
