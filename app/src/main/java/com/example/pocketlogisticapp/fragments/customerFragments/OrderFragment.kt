package com.example.pocketlogisticapp.fragments.customerFragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pocketlogisticapp.Adapters.OrderedProductAdapter
import com.example.pocketlogisticapp.MainActivity
import com.example.pocketlogisticapp.R
import com.example.pocketlogisticapp.TokenManager.TokensManager
import com.example.pocketlogisticapp.api.RetrofitClient
import com.example.pocketlogisticapp.model.OrderedProductsResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrderFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var displayThings: TextView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_order, container, false)

        recyclerView = view.findViewById(R.id.recyclerViewOrder)
        displayThings = view.findViewById(R.id.orderThings)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())



        fetchProducts()

        return view // ✅ THIS was missing
    }

    private fun fetchProducts() {
        val authHeader = TokensManager.getToken(requireContext()) ?: ""

        RetrofitClient.instance.getOrderedProducts(authHeader)
            .enqueue(object : Callback<OrderedProductsResponse> {
                override fun onResponse(
                    call: Call<OrderedProductsResponse>,
                    response: Response<OrderedProductsResponse>
                ) {
                    Log.d("Response","$response")
                    if (response.isSuccessful && response.body() != null) {
                        val products = response.body()!!.orders
                        if (products == null) {
                            displayThings.visibility = View.VISIBLE
                        } else {
                            displayThings.visibility = View.GONE


                            recyclerView.adapter = OrderedProductAdapter(products) { order ->
                                Toast.makeText(
                                    requireContext(),
                                    "Tracking order #${order._id}",
                                    Toast.LENGTH_SHORT
                                ).show()
                                // TODO: Add navigation to order tracking screen here
                            }
                        }
                    } else {
                        Log.e("OrderFetch", "Response failed: ${response.code()}")
                        Toast.makeText(requireContext(), "Failed to load orders", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<OrderedProductsResponse>, t: Throwable) {
                    Log.e("OrderFetch", "Error: ${t.message}")
                    Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }
}
