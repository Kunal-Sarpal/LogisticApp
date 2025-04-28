package com.example.pocketlogisticapp.dashbords

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

import androidx.fragment.app.Fragment
import com.example.pocketlogisticapp.R
import com.example.pocketlogisticapp.fragments.AdminFragments.AdminProfileFragment
import com.example.pocketlogisticapp.fragments.AdminFragments.AgentsListFragment
import com.example.pocketlogisticapp.fragments.AdminFragments.OrdersFragment
import com.example.pocketlogisticapp.fragments.AdminFragments.createProductFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class Admin : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_admin)


        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)

        loadFragment(OrdersFragment())

        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> loadFragment(OrdersFragment())
                R.id.nav_orders -> loadFragment(createProductFragment())
                R.id.nav_profile -> loadFragment(AdminProfileFragment())

            }
            true
        }
    }
    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.admin_fragment_container, fragment)
            .commit()
    }
}