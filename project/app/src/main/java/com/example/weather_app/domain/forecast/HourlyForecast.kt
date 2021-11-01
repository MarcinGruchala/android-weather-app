package com.example.weather_app.domain.forecast

data class HourlyForecast(
    val hour: String,
    val temp: Int,
    val icon: Int
)
