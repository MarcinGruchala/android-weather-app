package com.example.weather_app.di

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
        webservice: OpenWeatherAPIService
    ): RepositoryImpl = RepositoryImpl(webservice)

}
