package com.example.pocketlogisticapp.model

data class OrderedProductsResponse(
    val message: String,
    val products: List<OrderProduct>
)