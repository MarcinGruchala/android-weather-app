package com.example.weather_app.utils

import android.util.Log
import java.util.*

private const val TAG = "ClockUtils"
object ClockUtils {

    fun getDayFromUnixTimestamp(unixTimeStamp: Long): String{
        val calendar = Calendar.getInstance(Locale.ENGLISH)
        calendar.timeInMillis = (unixTimeStamp - TimeZone.getDefault().rawOffset-3600*1000L)
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

    fun getTimeFromUnixTimestamp(unixTimeStamp: Long, timeZone: Long, minutesMode: Boolean, clockPeriodMode: Boolean): String{
        Log.d(TAG,"Timezone: ${TimeZone.getDefault().rawOffset}")
        Log.d(TAG,"UTC timestamp: ${unixTimeStamp - TimeZone.getDefault().rawOffset}")
        val calendar = Calendar.getInstance(Locale.ENGLISH)

        calendar.timeInMillis = (unixTimeStamp - TimeZone.getDefault().rawOffset-3600*1000L) + timeZone

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
}
