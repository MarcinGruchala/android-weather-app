package com.example.weather_app.domain

data class HourlyForecastData(
    val hour: String,
    val temp: Int,
    val icon: Int
)
