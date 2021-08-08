package com.example.weather_app.di

import androidx.room.Room
import com.example.weather_app.database.dao.CityShortcutDao
import com.example.weather_app.database.CityShortcutDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideCityShortcutDao(
        cityShortcutDatabase: CityShortcutDatabase
    ): CityShortcutDao = cityShortcutDatabase.cityShortcutDao()

    @Singleton
    @Provides
    fun provideCityShortcutDatabase(
        applicationContext: WeatherApplication
    ): CityShortcutDatabase = Room.databaseBuilder(
        applicationContext,
        CityShortcutDatabase::class.java,
        "cities_shortcuts",
    )
        .fallbackToDestructiveMigration()
        .build()
}
