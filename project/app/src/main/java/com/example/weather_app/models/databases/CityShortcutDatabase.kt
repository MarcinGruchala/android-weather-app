package com.example.weather_app.models.databases

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.weather_app.models.dao.CityShortcutDao
import com.example.weather_app.models.entities.CityShortcut

@Database(entities = [CityShortcut::class], version = 1, exportSchema = false )
abstract class CityShortcutDatabase : RoomDatabase() {

    abstract fun cityShortcutDao(): CityShortcutDao
}