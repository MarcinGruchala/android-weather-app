package com.example.weather_app.utils

import android.util.Log
import java.sql.Date
import java.sql.Timestamp
import java.util.*

private const val TAG = "ClockUtils"
object ClockUtils {

    fun getDayFromUnixTimestamp(unixTimeStamp: Int): String{
        return when(val day = Timestamp(unixTimeStamp.toLong()*1000L).day){
            0 -> "Sunday"
            1 -> "Monday"
            2 -> "Tuesday"
            3 -> "Wednesday"
            4 -> "Thursday"
            5 -> "Friday"
            6 -> "Saturday"
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

    fun getTimeFromUnixTimestamp(unixTimeStamp: Long, minutesMode: Boolean, clockPeriodMode: Boolean): String{
        Log.d(TAG,"TimeStamp: $unixTimeStamp")
        val date = Date(unixTimeStamp)
        val calendar = Calendar.getInstance()
        calendar.time = date


        var hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minutes = calendar.get(Calendar.MINUTE)
        if (clockPeriodMode){
            val period = get12HourClockPeriod(hour)
            if( hour == 0 ) hour = 12
            if (minutesMode){
                return "$hour:$minutes $period"
            }
            return "$hour $period"
        }
        if (minutesMode){
            return "$hour:$minutes"
        }
        return "$hour"
    }

    fun getLocalTime(utcTime: Long, timeZone: Long): Long = utcTime + timeZone

}
