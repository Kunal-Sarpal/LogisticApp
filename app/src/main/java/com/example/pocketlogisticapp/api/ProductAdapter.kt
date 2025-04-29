package com.example.pocketlogisticapp.api

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pocketlogisticapp.R
import com.example.pocketlogisticapp.model.Product

class ProductAdapter(private val products: List<Product>) :
    RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    inner class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.productName)
        val price: TextView = view.findViewById(R.id.productPrice)
        val image: ImageView = view.findViewById(R.id.productImage)
        val button:Button = view.findViewById(R.id.trackButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]
        holder.name.text = product.title
        holder.price.text = "₹${product.price}"
        holder.button.isEnabled = false
        holder.button.text = "Buy"

        Glide.with(holder.image.context)
            .load(product.image)
            .placeholder(R.drawable.noimage) // optional
            .error(R.drawable.noimage)         // optional
            .into(holder.image)
    }

    override fun getItemCount(): Int = products.size
}