package com.example.pocketlogisticapp.fragments.AdminFragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pocketlogisticapp.Adapters.AdminOrdersAdapter
import com.example.pocketlogisticapp.R
import com.example.pocketlogisticapp.TokenManager.TokensManager
import com.example.pocketlogisticapp.api.RetrofitClient
import com.example.pocketlogisticapp.models.AllOrdersResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class OrdersFragment : Fragment() {


    private lateinit var ordersRecyclerView: RecyclerView
    private lateinit var orderAdapter: AdminOrdersAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_orders, container, false)
        ordersRecyclerView = view.findViewById(R.id.ordersRecyclerView)
        ordersRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        fetchOrders()

        return view
    }


    private fun fetchOrders() {
        val token = TokensManager.getToken(requireContext()) ?: ""
        val authHeader = token

        RetrofitClient.instance.getOrders(authHeader)
            .enqueue(object : Callback<AllOrdersResponse> {
                override fun onResponse(
                    call: Call<AllOrdersResponse>,
                    response: Response<AllOrdersResponse>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        val orders = response.body()!!.orders
                        ordersRecyclerView.adapter = AdminOrdersAdapter(orders,
                            onAssignClick = { order ->
                                Toast.makeText(requireContext(), "Assigning agent for order: ${order}", Toast.LENGTH_SHORT).show()

                            },
                            onStatusClick = { order ->
                                Toast.makeText(requireContext(), "Checking status for order: ${order}", Toast.LENGTH_SHORT).show()
                            }
                        )
                    } else {
                        Toast.makeText(requireContext(), "Failed to load orders", Toast.LENGTH_SHORT).show()
                        Log.e("OrdersAPI", "Error: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<AllOrdersResponse>, t: Throwable) {
                    Toast.makeText(requireContext(), "Error: ${t.localizedMessage}", Toast.LENGTH_SHORT).show()
                    Log.e("OrdersAPI", "Failure: ${t.message}")
                }
            })
    }
}