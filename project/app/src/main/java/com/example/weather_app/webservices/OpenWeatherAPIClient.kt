//package com.example.weather_app.webservices
//
//import retrofit2.Retrofit
//import retrofit2.converter.gson.GsonConverterFactory
//
//object OpenWeatherAPIClient {
//
//    val api: OpenWeatherAPIService by lazy {
//        Retrofit.Builder()
//            .baseUrl("https://api.openweathermap.org/data/2.5/")
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//            .create(OpenWeatherAPIService::class.java)
//    }
//
//}
