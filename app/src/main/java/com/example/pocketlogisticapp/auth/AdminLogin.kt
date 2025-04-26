package com.example.pocketlogisticapp.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.pocketlogisticapp.dashbords.Admin
import com.example.pocketlogisticapp.R
import com.example.pocketlogisticapp.TokenManager.TokensManager
import com.example.pocketlogisticapp.api.RetrofitClient
import com.example.pocketlogisticapp.model.LoginRequest
import com.example.pocketlogisticapp.model.LoginResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdminLogin : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_admin_login)
        val emailInput = findViewById<EditText>(R.id.editTextEmail)
        val passwordInput = findViewById<EditText>(R.id.editTextPassword)
        val loginButton = findViewById<Button>(R.id.buttonLogin)


        // Handle login button click
        loginButton.setOnClickListener {
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()

            // Basic validation for empty fields
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter both email and password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val loginRequest = LoginRequest(email, password)


            // Call the API with Retrofit
            RetrofitClient.instance.loginAdmin(loginRequest).enqueue(object :
                Callback<LoginResponse> {
                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    if (response.isSuccessful && response.body() != null) {
                        val token = response.body()?.token

                        if (token != null) {
                            // Store the token for the agent role using TokenManager
                            TokensManager.storeToken(this@AdminLogin, token, "Admin")

                            // Show success message and navigate to Agent dashboard
                            Toast.makeText(this@AdminLogin, "Login successful!", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this@AdminLogin, Admin::class.java))
                            finish()
                        } else {
                            Toast.makeText(this@AdminLogin, "Token not found in response", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this@AdminLogin, "Invalid credentials", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Toast.makeText(this@AdminLogin, "Login failed: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}