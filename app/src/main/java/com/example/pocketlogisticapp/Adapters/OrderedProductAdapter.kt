package com.example.pocketlogisticapp.Adapters

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pocketlogisticapp.R
import com.example.pocketlogisticapp.model.OrderProduct
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.w3c.dom.Text

class OrderedProductAdapter(
    private val products: List<OrderProduct>,
    private val onTrackClicked: (OrderProduct) -> Unit
) : RecyclerView.Adapter<OrderedProductAdapter.OrderedItemViewHolder>() {

    inner class OrderedItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title = itemView.findViewById<TextView>(R.id.productName)
        val price = itemView.findViewById<TextView>(R.id.productPrice)
        val image = itemView.findViewById<ImageView>(R.id.productImage)
        val trackBtn = itemView.findViewById<Button>(R.id.trackButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderedItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)
        return OrderedItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderedItemViewHolder, position: Int) {
        val product = products[position]
        holder.title.text = product.title
        holder.price.text = "₹${product.price}"
        Glide.with(holder.itemView.context).load(product.image).into(holder.image)
        Log.d("This is product id ",product._id)
        holder.trackBtn.setOnClickListener {
            showTrackingView(holder.itemView.context, product)
            onTrackClicked(product)
        }
    }

    private fun showTrackingView(context: Context, product: OrderProduct) {
        // Inflate the tracking layout
        val dialogView = LayoutInflater.from(context).inflate(R.layout.track_order, null)
        val orderId = dialogView.findViewById<TextView>(R.id.orderIdValue)
        val orderPrice = dialogView.findViewById<TextView>(R.id.priceValue)

        // Create and show dialog
        orderId.text = "${product._id}";
        orderPrice.text = "Rs. ${product.price}";

        val dialog = MaterialAlertDialogBuilder(context)
            .setView(dialogView)
            .setCancelable(true)
            .create()

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) // Optional: transparent corners
        dialog.show()


    }

    override fun getItemCount(): Int = products.size
}
