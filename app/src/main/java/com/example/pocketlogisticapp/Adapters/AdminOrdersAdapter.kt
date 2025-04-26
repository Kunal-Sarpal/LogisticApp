package com.example.pocketlogisticapp.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pocketlogisticapp.R
import com.example.pocketlogisticapp.models.SingleOrder

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

        // Set click listeners for buttons
        holder.btnAssignAgent.setOnClickListener {
            onAssignClick(order._id)
        }

        holder.btnCheckStatus.setOnClickListener {
            onStatusClick(order._id)
        }
    }

    override fun getItemCount(): Int = orders.size
}
