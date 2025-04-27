package com.example.pocketlogisticapp.fragments.AgentFragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.pocketlogisticapp.R
import com.example.pocketlogisticapp.TokenManager.TokensManager
import com.example.pocketlogisticapp.api.RetrofitClient
import com.example.pocketlogisticapp.model.LocationUpdateRequest
import com.example.pocketlogisticapp.model.ResponseMessage
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AgentUpdateFragment : Fragment() {

    private lateinit var headingText: TextView
    private lateinit var errorText: TextView
    private lateinit var noOrderMessage: TextView
    private lateinit var pickupLocationInput: EditText
    private lateinit var pickupPincodeInput: EditText
    private lateinit var updateButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_agent_update, container, false)

        // Bind Views
        headingText = view.findViewById(R.id.headingText)
        errorText = view.findViewById(R.id.errorText)
        noOrderMessage = view.findViewById(R.id.noOrderMessage)
        pickupLocationInput = view.findViewById(R.id.pickupLocationInput)
        pickupPincodeInput = view.findViewById(R.id.pickupPincodeInput)
        updateButton = view.findViewById(R.id.updateButton)

        // Check SharedPreferences for order status
        val sharedPref = requireActivity().getSharedPreferences("orderStatus", Context.MODE_PRIVATE)
        val orderStatus = sharedPref.getBoolean("order", false) // Default false if not found

        if (orderStatus) {
            // Order exists, show update form
            showOrderUpdateForm()
        } else {
            // No orders, show "wait for an order" message
            showNoOrderMessage()
        }

        // Button click listener for update action
        updateButton.setOnClickListener {
            if (isInputValid()) {
                // Proceed with update logic, like making API call, etc.
                updateAgentLocation()
            } else {
                errorText.visibility = View.VISIBLE
            }
        }

        return view
    }

    private fun showOrderUpdateForm() {
        // Hide error message and no order message
        errorText.visibility = View.GONE
        noOrderMessage.visibility = View.GONE

        // Show the fields to update
        pickupLocationInput.visibility = View.VISIBLE
        pickupPincodeInput.visibility = View.VISIBLE
        updateButton.visibility = View.VISIBLE

        // Set heading
        headingText.text = "Update Your Orders"
    }

    private fun showNoOrderMessage() {
        // Hide all order fields and show no order message
        pickupLocationInput.visibility = View.GONE
        pickupPincodeInput.visibility = View.GONE
        updateButton.visibility = View.GONE

        // Show the "no orders" message
        noOrderMessage.visibility = View.VISIBLE
        headingText.text = "No Orders Available"
    }

    private fun isInputValid(): Boolean {
        // Check if both fields have valid input
        val pickupLocation = pickupLocationInput.text.toString().trim()
        val pickupPincode = pickupPincodeInput.text.toString().trim()

        return pickupLocation.isNotEmpty() && pickupPincode.isNotEmpty()
    }

    private fun updateAgentLocation() {
        val token = TokensManager.getToken(requireContext()) ?: ""
        val authHeader = token
        // Get the agentId from SharedPreferences
        val sharedPref = requireActivity().getSharedPreferences("orderStatus", Context.MODE_PRIVATE)
        val agentId = sharedPref.getString("agentId", null) // Assuming agentId is saved as a string in SharedPreferences

        if (agentId != null) {
            // Get the values from the input fields
            val location = pickupLocationInput.text.toString().trim()
            val pincode = pickupPincodeInput.text.toString().trim()

            // Create the LocationUpdateRequest
            val locationUpdateRequest = LocationUpdateRequest(location, pincode)

            // Create the API request
            val updateCall = RetrofitClient.instance.updateAgentLocation(
                authHeader,
                agentId,
                locationUpdateRequest // Pass the LocationUpdateRequest as body
            )

            // Make the API call
            updateCall.enqueue(object : Callback<ResponseMessage> {
                override fun onResponse(call: Call<ResponseMessage>, response: Response<ResponseMessage>) {
                    if (response.isSuccessful) {
                        // Show success message
                        pickupLocationInput.text.clear()
                        pickupPincodeInput.text.clear()

                        Toast.makeText(requireContext(), "Location Updated Successfully!", Toast.LENGTH_SHORT).show()
                    } else {
                        // Handle failure
                        Toast.makeText(requireContext(), "Failed to update location.", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ResponseMessage>, t: Throwable) {
                    // Handle failure (network error)
                    Toast.makeText(requireContext(), "Network error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            Toast.makeText(requireContext(), "Agent ID not found.", Toast.LENGTH_SHORT).show()
        }
    }
}
