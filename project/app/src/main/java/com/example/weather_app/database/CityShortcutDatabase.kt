package com.example.weather_app.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.weather_app.database.dao.CityShortcutDao
import com.example.weather_app.database.entities.CityShortcut

@Database(
    entities = [CityShortcut::class],
    version = 2,
    exportSchema = false
)
abstract class  CityShortcutDatabase : RoomDatabase() {

    abstract fun cityShortcutDao(): CityShortcutDao
}
