package com.example.weather_app.webservices

import com.example.weather_app.webservices.model.current_weather_data.CurrentWeatherDataResponse
import com.example.weather_app.webservices.model.weather_forecast_data.WeatherForecastDataResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherAPIService {

    @GET("weather")
    suspend fun getCurrentWeatherData(
        @Query("q") city: String,
        @Query("appid") key: String,
        @Query("units") units: String
    ) : Response<CurrentWeatherDataResponse>

    @GET("onecall")
    suspend fun getWeatherForecastData(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("exclude") exclude: String,
        @Query("appid") key: String,
        @Query("units") units: String
    ) : Response<WeatherForecastDataResponse>
}
