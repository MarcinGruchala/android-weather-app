package com.example.weather_app.webservices.model.weather_forecast_data

data class Hourly(
    val clouds: Int,
    val dew_point: Double,
    val dt: Int,
    val feels_like: Double,
    val humidity: Int,
    val pop: Double,
    val pressure: Int,
    val rain: Rain,
    val temp: Double,
    val uvi: Double,
    val visibility: Int,
    val weather: List<WeatherXX>,
    val wind_deg: Int,
    val wind_gust: Double,
    val wind_speed: Double
)
