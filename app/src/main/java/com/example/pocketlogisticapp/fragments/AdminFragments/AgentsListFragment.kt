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
import com.example.pocketlogisticapp.Adapters.AgentAdapter
import com.example.pocketlogisticapp.R
import com.example.pocketlogisticapp.TokenManager.TokensManager
import com.example.pocketlogisticapp.api.RetrofitClient
import com.example.pocketlogisticapp.model.AgentListdata
import com.example.pocketlogisticapp.model.ApiAgent
import com.example.pocketlogisticapp.model.AssignAgentRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AgentsListFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private val agentList = mutableListOf<AgentListdata>()
    private var orderId: String? = null // Make it nullable to handle cases where it's not passed

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Get orderId from previous fragment
        orderId = arguments?.getString("orderId")
        Log.d("AgentsListFragment", "Received orderId: $orderId")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_agents_list, container, false)

        // If orderId is null or empty, show a message and return
        if (orderId.isNullOrEmpty()) {
            Toast.makeText(requireContext(), "No order ID received", Toast.LENGTH_SHORT).show()
            return view
        }

        recyclerView = view.findViewById(R.id.agentsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Fetch agents
        fetchAgents()

        return view
    }

    private fun fetchAgents() {
        val token = TokensManager.getToken(requireContext()) ?: ""
        val authHeader = token

        Log.d("AuthHeader", "Using token: $authHeader")

        RetrofitClient.instance.getAgents(authHeader).enqueue(object : Callback<ApiAgent> {
            override fun onResponse(call: Call<ApiAgent>, response: Response<ApiAgent>) {
                Log.d("Response", "Response body: ${response.body()}")

                if (response.isSuccessful) {
                    val agentsResponse = response.body()?.agents
                    if (agentsResponse != null && agentsResponse.isNotEmpty()) {
                        recyclerView.adapter = AgentAdapter(agentsResponse) { agentId ->
                            // When agent clicked
                            onAgentClick(agentId)
                        }
                    } else {
                        Toast.makeText(requireContext(), "No agents found", Toast.LENGTH_SHORT).show()
                        Log.e("AgentsFetch", "No agents in the response")
                    }
                } else {
                    Toast.makeText(requireContext(), "Failed to load agents", Toast.LENGTH_SHORT).show()
                    Log.e("AgentsFetch", "Response failed: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<ApiAgent>, t: Throwable) {
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                Log.e("AgentsFetch", "Error: ${t.message}")
            }
        })
    }

    private fun onAgentClick(agentId: String) {
        if (orderId.isNullOrEmpty()) {
            Toast.makeText(requireContext(), "Order ID missing", Toast.LENGTH_SHORT).show()
            return
        }

        assignAgentToOrder(orderId!!, agentId)
    }

    private fun assignAgentToOrder(orderId: String, agentId: String) {
        val token = TokensManager.getToken(requireContext()) ?: ""

        val request = AssignAgentRequest(orderId = orderId, agentId = agentId)

        RetrofitClient.instance.assignAgent(token, request)
            .enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Toast.makeText(requireContext(), "Agent assigned successfully!", Toast.LENGTH_SHORT).show()
                        requireActivity().supportFragmentManager.popBackStack() // Go back to previous fragment
                    } else {
                        Toast.makeText(requireContext(), "Failed to assign agent", Toast.LENGTH_SHORT).show()
                        Log.e("AssignAgent", "Response failed: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(requireContext(), "Error: ${t.localizedMessage}", Toast.LENGTH_SHORT).show()
                    Log.e("AssignAgent", "Error: ${t.localizedMessage}")
                }
            })
    }
}
