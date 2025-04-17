package com.example.pocketlogisticapp.api

import com.example.pocketlogisticapp.model.Product
import com.example.pocketlogisticapp.model.LoginRequest
import com.example.pocketlogisticapp.model.LoginResponse
import com.example.pocketlogisticapp.model.OrderedProductsResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {
    @GET("customer/products")
    fun getProducts(): Call<List<Product>>
    @GET("customer/order/products")
    fun getOrderedProducts(
        @Header("Authorization") token: String
    ): Call<OrderedProductsResponse>
    @POST("auth/customer/login")
    fun loginUser(@Body loginRequest: LoginRequest): Call<LoginResponse>
    @POST("auth/Agent/login")
    fun loginAgent(@Body loginRequest: LoginRequest): Call<LoginResponse>
}