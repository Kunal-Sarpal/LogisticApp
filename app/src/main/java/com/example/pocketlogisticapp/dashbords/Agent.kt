package com.example.pocketlogisticapp.dashbords

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.pocketlogisticapp.R
import com.example.pocketlogisticapp.fragments.AgentFragments.AgentHome1Fragment
import com.example.pocketlogisticapp.fragments.AgentFragments.AgentProfileFragment
import com.example.pocketlogisticapp.fragments.AgentFragments.AgentUpdateFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class Agent : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_agent)

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav_agent)

        loadFragment((AgentHome1Fragment()))

        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> loadFragment(AgentHome1Fragment())
                R.id.nav_orders -> loadFragment(AgentUpdateFragment())
                R.id.nav_profile -> loadFragment(AgentProfileFragment())
            }
            true
        }


    }
    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.agent_fragment_container, fragment)
            .commit()
    }
}