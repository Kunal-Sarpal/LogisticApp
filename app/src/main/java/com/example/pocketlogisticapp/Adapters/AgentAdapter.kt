package com.example.pocketlogisticapp.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pocketlogisticapp.R
import com.example.pocketlogisticapp.model.AgentListdata

class AgentAdapter(
    private val agentList: List<AgentListdata>,
    private val onClick: (agentId: String) -> Unit
) : RecyclerView.Adapter<AgentAdapter.AgentViewHolder>() {

    inner class AgentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.agentName)
        val email: TextView = itemView.findViewById(R.id.agentEmail)
        val area: TextView = itemView.findViewById(R.id.agentArea)
        val status: TextView = itemView.findViewById(R.id.agentStatus)
        val actionButton: Button = itemView.findViewById(R.id.agentActionButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AgentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_agent, parent, false)
        return AgentViewHolder(view)
    }

    override fun onBindViewHolder(holder: AgentViewHolder, position: Int) {
        val agent = agentList[position]
        holder.name.text = "Name: ${agent.name}"
        holder.email.text = "Email: ${agent.email}"
        holder.area.text = "Area: ${agent.area}"
        holder.status.text =
            if (agent.assignedOrders.isEmpty()) "Available" else "Not Available"

        holder.actionButton.isEnabled = false;
        holder.actionButton.setOnClickListener {
            onClick(agent._id)
        }
    }

    override fun getItemCount(): Int = agentList.size
}
