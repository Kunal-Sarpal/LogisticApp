package com.example.pocketlogisticapp.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import com.example.pocketlogisticapp.dashbords.Admin

import androidx.core.view.WindowInsetsCompat
import com.example.pocketlogisticapp.R

class AdminLogin : AppCompatActivity() {

    private val adminUsers = mapOf(
        "sarpalkunal7@gmail.com" to "kunal@123",
        "mukand7@gmail.com" to "mukand@123"
    )
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_admin_login)
        val emailInput = findViewById<EditText>(R.id.editTextEmail)
        val passwordInput = findViewById<EditText>(R.id.editTextPassword)
        val loginButton = findViewById<Button>(R.id.buttonLogin)
        loginButton.setOnClickListener {
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter both email and password", Toast.LENGTH_SHORT).show()
            } else if (adminUsers[email] == password) {
                Toast.makeText(this, "Welcome Admin!", Toast.LENGTH_SHORT).show()

                // Redirect to Admin Dashboard or MainActivity
                startActivity(Intent(this, Admin::class.java))
                finish()
            } else {
                Toast.makeText(this, "Invalid credentials for admin", Toast.LENGTH_SHORT).show()
            }
        }
    }
}