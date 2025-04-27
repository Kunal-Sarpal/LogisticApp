package com.example.pocketlogisticapp.fragments.AgentFragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.pocketlogisticapp.R
import com.example.pocketlogisticapp.TokenManager.TokensManager
import com.example.pocketlogisticapp.api.RetrofitClient
import com.example.pocketlogisticapp.model.AgentOrderModel
import com.example.pocketlogisticapp.model.AssignedOrder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AgentHome1Fragment : Fragment() {

    private lateinit var username: TextView
    private lateinit var useremail: TextView
    private lateinit var productName: TextView
    private lateinit var productImage: ImageView
    private lateinit var pickupLocation: TextView
    private lateinit var deliveryLocation: TextView
    private lateinit var orderContainer: ScrollView
    private lateinit var noOrderMessage: TextView
    private lateinit var orderIdViewText: TextView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_agent_home1, container, false)

        // bind views
        username        = view.findViewById(R.id.username)
        useremail       = view.findViewById(R.id.useremail)
        productName     = view.findViewById(R.id.productName)
        productImage    = view.findViewById(R.id.productImage)
        pickupLocation  = view.findViewById(R.id.pickupLocation)
        deliveryLocation= view.findViewById(R.id.deliveryLocation)
        orderContainer  = view.findViewById(R.id.orderContainer)
        noOrderMessage  = view.findViewById(R.id.noOrderMessage)
        orderIdViewText = view.findViewById(R.id.orderId)

        fetchOrder()
        return view
    }

    private fun fetchOrder() {
        val token      = TokensManager.getToken(requireContext()) ?: ""
        val authHeader = "$token"
        Log.d("AgentHome1", "Token: $authHeader")

        RetrofitClient.instance
            .getAgentOrders(authHeader)
            .enqueue(object : Callback<AgentOrderModel> {
                override fun onResponse(
                    call: Call<AgentOrderModel>,
                    response: Response<AgentOrderModel>
                ) {
                    Log.d("AgentHome1", "Response code: ${response.code()}")
                    if (response.isSuccessful) {

                        val orders = response.body()?.assignedOrders.orEmpty()
                        if (orders.isNotEmpty()) {
                            val agentId = response.body()?.agentId  // Assuming the API response contains agentId
                            if (agentId != null) {
                                val sharedPreferences = requireContext().getSharedPreferences("orderStatus", Context.MODE_PRIVATE)
                                val editor = sharedPreferences.edit()
                                editor.putString("agentId", agentId)  // Save agentId
                                editor.apply()
                            }
                            // show order UI
                            orderContainer.visibility = View.VISIBLE
                            noOrderMessage.visibility = View.GONE
                            populateOrder(orders[0])
                        } else {
                            // 200 OK but empty list
                            showNoOrdersMessage()
                        }
                    } else {
                        // non-200 (404, 401, etc)
                        showNoOrdersMessage()
                    }
                }

                override fun onFailure(call: Call<AgentOrderModel>, t: Throwable) {
                    Log.e("AgentHome1", "Network error", t)
                    showNoOrdersMessage()
                }
            })
    }

    private fun populateOrder(order: AssignedOrder) {
        username.text       = order.userId.name
        useremail.text      = order.userId.email
        productName.text    = order.productId.title
        orderIdViewText.text = order._id

        val sharedPreferences = requireContext().getSharedPreferences("orderStatus", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean("order", true)  // Save true when the order is fetched
        editor.apply()

        Glide.with(this)
            .load(order.productId.image)
            .into(productImage)
        pickupLocation.text   = "USA,California,Bay Area"
        deliveryLocation.text = "${order.userId.city}, ${order.userId.state}, ${order.userId.pincode}"
    }

    private fun showNoOrdersMessage() {
        orderContainer.visibility = View.GONE
        noOrderMessage.visibility = View.VISIBLE
        val sharedPreferences = requireContext().getSharedPreferences("orderStatus", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean("order", false)  // Save false when no order is available
        editor.apply()
    }
}
