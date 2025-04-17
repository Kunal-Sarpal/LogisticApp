package com.example.pocketlogisticapp.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    // Define the base URL
    private const val BASE_URL = "https://api.pocketindia.shop/api/v1/"

    // Lazy initialization of Retrofit instance
    val instance: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
