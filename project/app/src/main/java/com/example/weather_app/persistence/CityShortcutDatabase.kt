package com.example.weather_app.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.weather_app.persistence.dao.CityShortcutDao
import com.example.weather_app.persistence.entities.CityShortcut

@Database(
    entities = [CityShortcut::class],
    version = 2,
    exportSchema = false
)
abstract class  CityShortcutDatabase : RoomDatabase() {

    abstract fun cityShortcutDao(): CityShortcutDao
}
