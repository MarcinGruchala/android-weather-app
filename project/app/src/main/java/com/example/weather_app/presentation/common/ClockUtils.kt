package com.example.weather_app.presentation.common

import java.util.*

object ClockUtils {

    var deviceTimezone = TimeZone.getDefault().rawOffset

    fun getDayFromUnixTimestamp(
        unixTimeStamp: Long,
        timeZone: Long,
        deviceTimezone: Long
    ): String {
        val calendar = Calendar.getInstance(Locale.ENGLISH)
        calendar.timeInMillis = (unixTimeStamp - deviceTimezone) + timeZone
        return when(val day = calendar.get(Calendar.DAY_OF_WEEK)){
            1 -> "Sunday"
            2 -> "Monday"
            3 -> "Tuesday"
            4 -> "Wednesday"
            5 -> "Thursday"
            6 -> "Friday"
            7 -> "Saturday"
            else -> "Day: $day"
        }
    }

    private fun get12HourClockPeriod(hour: Int): String{
        return when(hour){
            in 0..11, 24 -> "AM"
            in 12..23 -> "PM"
            else -> hour.toString()
        }
    }

    fun getTimeFromUnixTimestamp(
        unixTimeStamp: Long,
        timeZone: Long,
        deviceTimezone: Long,
        minutesMode: Boolean,
        clockPeriodMode: Boolean
    ): String{
        val calendar = Calendar.getInstance(Locale.ENGLISH)
        calendar.timeInMillis = (unixTimeStamp - deviceTimezone) + timeZone

        var hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minutes = calendar.get(Calendar.MINUTE)
        if (clockPeriodMode) {
            val period = get12HourClockPeriod(hour)
            if( hour == 0 ) {
                hour = 12
            }
            if (minutesMode) {
                return "${getClockString(hour)}:${getClockString(minutes)} $period"
            }
            return "$hour $period"
        }
        if (minutesMode) {
            return "${getClockString(hour)}:${getClockString(minutes)}"
        }
        return "$hour"
    }

    private fun getClockString(timeUnit: Int): String{
        if(timeUnit<10){
            return "0$timeUnit"
        }
        return "$timeUnit"
    }
}
