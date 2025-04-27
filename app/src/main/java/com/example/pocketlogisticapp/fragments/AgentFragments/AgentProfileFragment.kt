package com.example.pocketlogisticapp.fragments.AgentFragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.pocketlogisticapp.MainActivity
import com.example.pocketlogisticapp.R
import com.example.pocketlogisticapp.TokenManager.TokensManager


class AgentProfileFragment : Fragment() {
    private lateinit var btn: Button
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_agent_profile, container, false)
        btn = view.findViewById(R.id.btnLogout)
        btn.setOnClickListener {
            TokensManager.clearTokens(requireContext())
            startActivity(Intent(requireContext(), MainActivity::class.java))
            requireActivity().finish() // Optional: prevent going back
        }
        return view
    }
}