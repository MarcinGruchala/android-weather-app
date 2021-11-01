package com.example.weather_app.persistence.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.weather_app.persistence.shortcut.CityShortcutDao
import com.example.weather_app.persistence.shortcut.CityShortcutEntity

@Database(
    entities = [CityShortcutEntity::class],
    version = 2,
    exportSchema = false
)
abstract class  CityShortcutDatabase : RoomDatabase() {

    abstract fun cityShortcutDao(): CityShortcutDao
}
