package com.example.pocketlogisticapp.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pocketlogisticapp.R
import com.example.pocketlogisticapp.TokenManager.TokensManager
import com.example.pocketlogisticapp.api.RetrofitClient
import com.example.pocketlogisticapp.models.SingleOrder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.util.Log

class AdminOrdersAdapter(
    private val orders: List<SingleOrder>,
    private val onAssignClick: (String) -> Unit,
    private val onStatusClick: (String) -> Unit
) : RecyclerView.Adapter<AdminOrdersAdapter.OrderViewHolder>() {

    inner class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Views from the XML
        val productName: TextView = itemView.findViewById(R.id.tvProductName)
        val price: TextView = itemView.findViewById(R.id.tvPrice)
        val upiId: TextView = itemView.findViewById(R.id.tvUpiId)
        val phone: TextView = itemView.findViewById(R.id.tvPhone)
        val btnAssignAgent: Button = itemView.findViewById(R.id.btnAssignAgent)
        val btnCheckStatus: Button = itemView.findViewById(R.id.btnCheckStatus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_admin_order, parent, false)
        return OrderViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = orders[position]

        // Binding data to the views
        holder.productName.text = "Product: ${order.productId.title}"
        holder.price.text = "Price: ₹${order.productId.price}"
        holder.upiId.text = "UPI: ${order.upiTransactionId}"
        holder.phone.text = "Phone: ${order.userId.phone}"

        // Disable the button by default
        holder.btnAssignAgent.isEnabled = false

        // Fetch the order status
        fetchOrderStatus(holder.itemView, order._id) { status ->
            // Enable the button if status is not 'assigned'
            if (status != "assigned") {
                holder.btnAssignAgent.isEnabled = true
            }
        }

        // Set click listeners for buttons
        holder.btnAssignAgent.setOnClickListener {
            onAssignClick(order._id)
        }

        holder.btnCheckStatus.setOnClickListener {
            onStatusClick(order._id)
        }
    }

    private fun fetchOrderStatus(itemView: View, orderId: String, callback: (String) -> Unit) {
        val context = itemView.context
        val token = TokensManager.getToken(context) ?: ""
        val authHeader = token

        RetrofitClient.instance.getOrderStatus(authHeader, orderId)
            .enqueue(object : Callback<Map<String, Any>> {
                override fun onResponse(
                    call: Call<Map<String, Any>>,
                    response: Response<Map<String, Any>>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        val status = response.body()?.get("status")?.toString() ?: "Not Assigned"
                        callback(status)
                    } else {
                        Log.e("OrderStatusAPI", "Error: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<Map<String, Any>>, t: Throwable) {
                    Log.e("OrderStatusAPI", "Failure: ${t.message}")
                }
            })
    }

    override fun getItemCount(): Int = orders.size
}
