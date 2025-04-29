package com.example.pocketlogisticapp.model

data class OrderedProductsResponse(
    val message: String,
    val orders: List<OrderProduct>
)