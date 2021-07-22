package com.example.weather_app.utils

import com.example.weather_app.R

object UiUtils {

    fun getWeatherIcon(iconTag: String): Int{
        return when(iconTag){
            "01d" -> R.drawable.ic_sun_custom_big_foreground
            "01n" -> R.drawable.ic_moon_custom_big_foreground
            "02d" -> R.drawable.few_clouds_day
            "02n" -> R.drawable.few_clouds_night
            "03d", "03n" -> R.drawable.scattered_clouds
            "04d", "04n" -> R.drawable.broken_clouds
            "09d", "09n" -> R.drawable.shower_rain
            "10d" -> R.drawable.rain_day
            "10n" -> R.drawable.rain_night
            "11d", "11n" -> R.drawable.thunderstorm
            "13d", "13n" -> R.drawable.snow
            "50d", "50n" -> R.drawable.mist
            else -> R.drawable.ic_few_clouds_dark
        }
    }
}
