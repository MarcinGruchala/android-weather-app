package com.example.weather_app.webservices.entities.currentweatherdata

data class Weather(
    val description: String,
    val icon: String,
    val id: Int,
    val main: String
)
