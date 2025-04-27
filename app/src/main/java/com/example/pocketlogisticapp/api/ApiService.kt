package com.example.pocketlogisticapp.api

import com.example.pocketlogisticapp.model.AgentOrderModel
import com.example.pocketlogisticapp.model.ApiAgent
import com.example.pocketlogisticapp.model.LocationUpdateRequest
import com.example.pocketlogisticapp.model.Product
import com.example.pocketlogisticapp.model.LoginRequest
import com.example.pocketlogisticapp.model.LoginResponse
import com.example.pocketlogisticapp.model.OrderedProductsResponse
import com.example.pocketlogisticapp.model.ResponseMessage
import com.example.pocketlogisticapp.models.AllOrdersResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

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
    @POST("admin/register")
    fun loginAdmin(@Body loginRequest: LoginRequest): Call<LoginResponse>
    @GET ("admin/get/agents")
    fun getAgents(
        @Header("Authorization") token: String
    ): Call<ApiAgent>
    @GET ("admin/get/orders")
    fun getOrders(
        @Header("Authorization") token: String
    ): Call<AllOrdersResponse>
    @GET("agent/orders")
    fun getAgentOrders(
        @Header("Authorization") token: String
    ): Call<AgentOrderModel>
    @POST("agent/update-location")
    fun updateAgentLocation(
        @Header("Authorization") token: String,
        @Query("agentId") agentId: String,  // Pass agentId in the query parameter
        @Body locationUpdateRequest: LocationUpdateRequest  // Pass the location update data in the body
    ): Call<ResponseMessage>

    @GET("admin/get/order/status")
    fun getOrderStatus(
        @Header("Authorization") token: String,
        @Query("orderId") orderId: String
    ): Call<Map<String, Any>>


}