package com.example.weather_app.networking.entities.currentweatherdata

data class Sys(
    val country: String,
    val id: Int,
    val sunrise: Int,
    val sunset: Int,
    val type: Int
)
