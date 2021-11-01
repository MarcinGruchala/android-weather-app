package com.example.weather_app.presentation.common

import com.example.weather_app.R

object UiUtils {

    private val NIGHT_TAGS = listOf(
        "01n","02n","03n",
        "04n","09n","10n",
        "11n","13n","50n"
    )

    private val GOOD_WEATHER_TAGS = listOf(
        "01d","02d"
    )

    private val BAD_WEATHER_TAGS = listOf(
        "03d","04d","09d","10d","11d","13d","50d"
    )

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
            else -> R.drawable.few_clouds_day
        }
    }

    fun getWeatherForecastBackground(iconTag: String): Int{
        return when(iconTag){
            in NIGHT_TAGS ->
                R.drawable.gradient_background_night
            in GOOD_WEATHER_TAGS ->
                R.drawable.gradient_background_good_weather
            in BAD_WEATHER_TAGS ->
                R.drawable.gradient_background_bad_weather
            else -> R.drawable.gradient_background_good_weather
        }
    }

    fun getCityShortcutBackground(iconTag: String): Int {
        return when(iconTag){
            in NIGHT_TAGS ->
                R.drawable.gradient_city_shortcut_night
            in GOOD_WEATHER_TAGS ->
                R.drawable.gradient_city_shortcut_good_weather
            in BAD_WEATHER_TAGS ->
                R.drawable.gradient_city_shortcut_bad_weather
            else ->
                R.drawable.gradient_city_shortcut_good_weather
        }
    }

    fun getStatusBarColor(iconTag: String): Int {
        return when(iconTag){
            in NIGHT_TAGS ->
                R.color.night_status_bar
            in GOOD_WEATHER_TAGS ->
                R.color.good_weather_status_bar
            in BAD_WEATHER_TAGS ->
                R.color.bad_weather_status_bar
            else ->
                R.color.good_weather_status_bar
        }
    }

    fun getHeaderColor(iconTag: String): Int {
        return when(iconTag) {
            in NIGHT_TAGS ->
                R.color.night_weather_header
            in GOOD_WEATHER_TAGS ->
                R.color.good_weather_header
            in BAD_WEATHER_TAGS ->
                R.color.bad_weather_header
            else ->
                R.color.good_weather_header
        }
    }
}
