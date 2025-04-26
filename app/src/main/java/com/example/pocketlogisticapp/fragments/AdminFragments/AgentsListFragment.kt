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
import retrofit2.Callback
import com.example.pocketlogisticapp.model.AgentListdata
import com.example.pocketlogisticapp.model.ApiAgent
import retrofit2.Call
import retrofit2.Response


class AgentsListFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private val agentList = mutableListOf<AgentListdata>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_agents_list, container, false)
        recyclerView = view.findViewById(R.id.agentsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        fetchAgents()
        return view
    }
    private fun fetchAgents() {
        val token = TokensManager.getToken(requireContext()) ?: ""
        val authHeader = token // Assuming token needs to be prefixed with "Bearer"

        Log.d("AuthHeader", "Using token: $authHeader") // Log the token for debugging

        RetrofitClient.instance.getAgents(authHeader).enqueue(object : Callback<ApiAgent> {
            override fun onResponse(call: Call<ApiAgent>, response: Response<ApiAgent>) {
                Log.d("Response", "Response body: ${response.body()}") // Log the full response body

                if (response.isSuccessful) {
                    Toast.makeText(requireContext(), "Agents found", Toast.LENGTH_SHORT).show()
                    val agentsResponse = response.body()!!.agents
                    if (agentsResponse != null && agentsResponse.isNotEmpty()) {
                        val agents: List<AgentListdata> = agentsResponse
                        recyclerView.adapter = AgentAdapter(agents) { agentId ->
                            Toast.makeText(
                                requireContext(),
                                "Agent clicked: $agentId",
                                Toast.LENGTH_SHORT
                            ).show()
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



}