package com.example.pocketlogisticapp

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pocketlogisticapp.TokenManager.TokensManager
import com.example.pocketlogisticapp.auth.AdminLogin
import com.example.pocketlogisticapp.auth.AgentLogin
import com.example.pocketlogisticapp.auth.CustomerLogin
import com.example.pocketlogisticapp.dashbords.Admin
import com.example.pocketlogisticapp.dashbords.Agent
import com.example.pocketlogisticapp.dashbords.Customer

class MainActivity : AppCompatActivity() {
    private lateinit var spinner: Spinner
    private lateinit var continueBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        spinner = findViewById(R.id.userTypeSpinner)
        continueBtn = findViewById(R.id.continueButton)

        // Spinner dropdown options
        val roles = arrayOf("Select Role", "Customer", "Admin", "Agent")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, roles)
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line    ,)
        spinner.adapter = adapter

        // 🔥 If user already logged in, skip to dashboard directly
        val token = TokensManager.getToken(this)
        val currentRole = TokensManager.getCurrentRole(this)

        if (token != null && currentRole != null) {
            when (currentRole) {
                "Customer" -> {
                    startActivity(Intent(this, Customer::class.java))
                    finish()
                }
                "Agent" -> {
                    startActivity(Intent(this, Agent::class.java))
                    finish()
                }
                "Admin" -> {
                    // Replace with actual Admin Dashboard if any
                    startActivity(Intent(this, Admin::class.java))
                    finish()
                }
            }
        }

        // 🔁 Login selector logic
        continueBtn.setOnClickListener {
            when (spinner.selectedItem.toString()) {
                "Customer" -> {
                    startActivity(Intent(this, CustomerLogin::class.java))
                }
                "Agent" -> {
                    startActivity(Intent(this, AgentLogin::class.java))
                }
                "Admin" -> {
                    startActivity(Intent(this, AdminLogin::class.java))
                }
                else -> {
                    Toast.makeText(this, "Please select a valid role", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
