package com.example.weather_app.persistence.shortcut

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cities_shortcuts")
data class CityShortcutEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val cityName: String,
    var localTime: String,
    var temp: Int,
    var icon: String
)
