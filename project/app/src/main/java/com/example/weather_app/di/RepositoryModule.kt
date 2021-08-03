package com.example.weather_app.di

import android.content.SharedPreferences
import com.example.weather_app.models.UnitOfMeasurement
import com.example.weather_app.models.dao.CityShortcutDao
import com.example.weather_app.repository.RepositoryImpl
import com.example.weather_app.webservices.OpenWeatherAPIService
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
