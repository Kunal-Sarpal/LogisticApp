package com.example.pocketlogisticapp.dashbords

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.pocketlogisticapp.R
import com.example.pocketlogisticapp.fragments.customerFragments.HomeFragment
import com.example.pocketlogisticapp.fragments.customerFragments.OrderFragment
import com.example.pocketlogisticapp.fragments.customerFragments.ProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationView


class Customer : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer)

        val bottomNav = findViewById<BottomNavigationView>(R.id.customer_bottom_nav)

        loadFragment(HomeFragment())

        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> loadFragment(HomeFragment())
                R.id.nav_orders -> loadFragment(OrderFragment())
                R.id.nav_profile -> loadFragment(ProfileFragment())
            }
            true
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.customer_fragment_container, fragment)
            .commit()
    }
}
