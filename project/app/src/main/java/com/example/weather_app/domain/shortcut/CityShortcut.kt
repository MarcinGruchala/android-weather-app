package com.example.weather_app.domain.shortcut

data class CityShortcut(
    val cityName: String,
    var localTime: String,
    var temp: Int,
    var icon: String
)
