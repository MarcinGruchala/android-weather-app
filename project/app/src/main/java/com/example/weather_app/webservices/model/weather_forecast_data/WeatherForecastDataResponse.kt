package com.example.weather_app.webservices.model.weather_forecast_data

data class WeatherForecastDataResponse(
    val daily: List<Daily>,
    val hourly: List<Hourly>,
    val lat: Double,
    val lon: Double,
    val timezone: String,
    val timezone_offset: Int
)
