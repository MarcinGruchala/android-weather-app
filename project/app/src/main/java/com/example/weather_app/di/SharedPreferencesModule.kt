package com.example.weather_app.di

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SharedPreferencesModule {

    @Singleton
    @Provides
    fun provideUnitsOfMeasurementSharedPreferences(application: WeatherApplication)
    : SharedPreferences = application.getSharedPreferences(
        "unitOfMeasurement",
        Context.MODE_PRIVATE
    )

    @Singleton
    @Provides
    fun provideUnitsOfMeasurementSharedPreferencesEditor(sharedPreferences: SharedPreferences)
    : SharedPreferences.Editor = sharedPreferences.edit()

}
