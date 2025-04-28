package com.example.pocketlogisticapp.model

data class AssignAgentRequest(
    val orderId: String,
    val agentId: String
)