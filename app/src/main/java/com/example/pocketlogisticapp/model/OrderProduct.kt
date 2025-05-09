package com.example.pocketlogisticapp.model

data class OrderProduct(
    val _id: String,
    val productId: ProductDetail
)

data class ProductDetail(
    val _id: String,
    val title: String,
    val price: Double,
    val image: String
)

