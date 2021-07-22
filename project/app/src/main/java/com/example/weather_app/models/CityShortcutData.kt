package com.example.weather_app.models

data class CityShortcutData(
    val cityName: String,
    var localTime: String,
    var temp: Int,
    var icon: Int
)