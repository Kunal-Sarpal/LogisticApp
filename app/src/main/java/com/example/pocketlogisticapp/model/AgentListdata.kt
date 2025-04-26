package com.example.pocketlogisticapp.model

data class AgentListdata(
    val _id: String,     // Unique identifier for each agent
    val name: String,    // Name of the agent
    val email: String,   // Email of the agent
    val password: String, // Password of the agent (encrypted)
    val phone: String,   // Phone number of the agent
    val area: String,    // Area location of the agent
    val assignedOrders: List<Order> // Assigned orders if there are any
)

data class Order(
    val status: String,  // Order status (e.g., "assigned")
    val _id: String      // Order ID
)