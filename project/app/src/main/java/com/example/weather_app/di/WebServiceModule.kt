package com.example.weather_app.di

import com.example.weather_app.BuildConfig
import com.example.weather_app.webservices.OpenWeatherAPIService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object WebServiceModule {

    @Singleton
    @Provides
    fun provideOpenWeatherApiService(): OpenWeatherAPIService = Retrofit.Builder()
        .baseUrl("https://api.openweathermap.org/data/2.5/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(OpenWeatherAPIService::class.java)

    @Singleton
    @Provides
    fun provideApiKey(): String = BuildConfig.APIKEY

}
