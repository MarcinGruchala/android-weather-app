package com.example.weather_app.domain

data class DailyForecastData(
    val day: String,
    val tempH: Int,
    val tempL: Int,
    val icon: Int
)
