package com.example.weather_app.models.databases

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.weather_app.models.dao.CityShortcutDao
import com.example.weather_app.models.entities.CityShortcut

@Database(entities = [CityShortcut::class], version = 1, exportSchema = false )
abstract class CityShortcutDatabase : RoomDatabase() {

    abstract fun cityShortcutDao(): CityShortcutDao

    companion object{

        @Volatile
        private var INSTANCE: CityShortcutDatabase? = null

        fun getDatabase(context: Context): CityShortcutDatabase{
            val tmpInstance = INSTANCE
            if (tmpInstance != null){
                return tmpInstance
            }
            synchronized(this){
                val instance =  Room.databaseBuilder(
                    context.applicationContext,
                    CityShortcutDatabase::class.java,
                    "cities_shortcuts"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}