package com.example.weather_app.webservices.model.current_weather_data

data class Sys(
    val country: String,
    val id: Int,
    val sunrise: Int,
    val sunset: Int,
    val type: Int
)