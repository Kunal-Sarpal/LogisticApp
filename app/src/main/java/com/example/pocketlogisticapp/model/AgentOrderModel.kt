package com.example.pocketlogisticapp.model

data class AgentOrderModel(
    val message: String,
    val agentId: String,
    val agentCity: String,
    val agentPincode: String,
    val assignedAt: String,
    val assignedOrders: List<AssignedOrder>
)

data class AssignedOrder(
    val _id: String,
    val userId: AgentUserId,
    val productId: AgentProductId,
)

data class AgentUserId(
    val city: String,
    val state: String,
    val pincode: String,
    val email: String,
    val name: String
)

data class AgentProductId(
    val title: String,
    val price: Int,
    val image: String
)