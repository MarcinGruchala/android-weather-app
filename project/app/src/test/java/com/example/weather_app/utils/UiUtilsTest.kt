package com.example.weather_app.utils

import com.example.weather_app.R
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class UiUtilsTest {

    @Test
    fun getWeatherIcon() {
        val result = UiUtils.getWeatherIcon(
            "01d"
        )
        assertThat(result).isEqualTo(R.drawable.ic_sun_custom_big_foreground)
    }

    @Test
    fun getWeatherForecastBackground() {
    }

    @Test
    fun getCityShortcutBackground() {
    }

    @Test
    fun getStatusBarColor() {
    }

    @Test
    fun getHeaderColor() {
    }
}