package com.example.weather_app.domain

import android.content.SharedPreferences
import com.example.weather_app.persistence.shortcut.CityShortcutDao
import com.example.weather_app.networking.OpenWeatherAPIService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideRepository(
        webservice: OpenWeatherAPIService,
        cityShortcutDao: CityShortcutDao,
        unitOfMeasurementSP: SharedPreferences
    ): RepositoryImpl = RepositoryImpl(
        webservice,
        cityShortcutDao,
        unitOfMeasurementSP
    )
}
