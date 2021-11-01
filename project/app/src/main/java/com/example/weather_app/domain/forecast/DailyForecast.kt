package com.example.weather_app.domain.forecast

data class DailyForecast(
    val day: String,
    val tempH: Int,
    val tempL: Int,
    val icon: Int
)
