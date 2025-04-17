package com.example.pocketlogisticapp.auth

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pocketlogisticapp.R
import com.example.pocketlogisticapp.TokenManager.TokensManager
import com.example.pocketlogisticapp.api.RetrofitClient
import com.example.pocketlogisticapp.dashbords.Agent
import com.example.pocketlogisticapp.model.LoginRequest
import com.example.pocketlogisticapp.model.LoginResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AgentLogin : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agent_login)

        val emailInput = findViewById<EditText>(R.id.editTextEmail)
        val passwordInput = findViewById<EditText>(R.id.editTextPassword)
        val loginButton = findViewById<Button>(R.id.buttonLogin)
        val renderToSignUp = findViewById<TextView>(R.id.textViewSignUp)

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
            renderToSignUp.setOnClickListener{
                val url = "https://pocketindia.shop/signup"
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(intent)
            }

            // Call the API with Retrofit
            RetrofitClient.instance.loginAgent(loginRequest).enqueue(object : Callback<LoginResponse> {
                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    if (response.isSuccessful && response.body() != null) {
                        val token = response.body()?.token

                        if (token != null) {
                            // Store the token for the agent role using TokenManager
                            TokensManager.storeToken(this@AgentLogin, token, "Agent")

                            // Show success message and navigate to Agent dashboard
                            Toast.makeText(this@AgentLogin, "Login successful!", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this@AgentLogin, Agent::class.java))
                            finish()
                        } else {
                            Toast.makeText(this@AgentLogin, "Token not found in response", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this@AgentLogin, "Invalid credentials", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Toast.makeText(this@AgentLogin, "Login failed: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}
