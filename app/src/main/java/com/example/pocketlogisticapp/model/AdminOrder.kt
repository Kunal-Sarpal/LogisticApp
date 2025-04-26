package com.example.pocketlogisticapp.models

data class AllOrdersResponse(
    val orders: List<SingleOrder>
)

data class SingleOrder(
    val _id:String,
    val upiTransactionId: String,
    val productId: ProductNext,
    val userId: UserNext
)

data class ProductNext(
    val _id: String,
    val title: String,
    val price: Int
)

data class UserNext(
    val phone: String
)
