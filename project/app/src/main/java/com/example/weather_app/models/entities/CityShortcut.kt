package com.example.weather_app.models.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cities_shortcuts")
data class CityShortcut(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val cityName: String,
    var localTime: String,
    var temp: Int,
    var icon: String
)
