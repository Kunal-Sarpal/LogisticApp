package com.example.pocketlogisticapp.model

// A model class representing the response from the server after updating agent's location.
data class ResponseMessage(
    val message: String,
    val agent: Agent? = null
)

// Model class representing an Agent.
data class Agent(
    val id: String,               // Agent ID
    val name: String,             // Agent Name
    val email: String,            // Agent Email
    val phone: String,            // Agent Phone number
    val pincode: String,          // Agent's Pincode
    val city: String              // Agent's City
)

// Model class representing the location update request (if needed to be sent from the client).
data class LocationUpdateRequest(
    val location: String,        // New location (city)
    val pincode: String          // New pincode
)

// Model class for any general error response.
data class ErrorResponse(
    val message: String          // Error message if any
)
