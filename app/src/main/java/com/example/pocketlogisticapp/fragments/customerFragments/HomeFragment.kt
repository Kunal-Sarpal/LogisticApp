package com.example.pocketlogisticapp.fragments.customerFragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pocketlogisticapp.R
import com.example.pocketlogisticapp.TokenManager.TokensManager
import com.example.pocketlogisticapp.api.ProductAdapter
import com.example.pocketlogisticapp.api.RetrofitClient
import com.example.pocketlogisticapp.model.Product
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ProductAdapter
    private lateinit var tokenTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // Initialize views
        recyclerView = view.findViewById(R.id.recyclerView)
        tokenTextView = view.findViewById(R.id.tokenTextView)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Set token
        val token = TokensManager.getToken(requireContext())
        tokenTextView.text = "Token: ${token?.take(30)}"
        Toast.makeText(requireContext(), "Token: ${token?.take(30)}...", Toast.LENGTH_LONG).show()

        // Fetch API data
        fetchProducts()

        return view
    }

    private fun fetchProducts() {
        RetrofitClient.instance.getProducts().enqueue(object : Callback<List<Product>> {
            override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {
                if (response.isSuccessful && response.body() != null) {
                    val products = response.body()!!
                    adapter = ProductAdapter(products)
                    recyclerView.adapter = adapter
                } else {
                    Log.e("API Error", "Response not successful: ${response.code()}")
                    Toast.makeText(requireContext(), "Failed to load products", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }
}
