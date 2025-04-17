package com.example.pocketlogisticapp.auth

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pocketlogisticapp.R
import com.example.pocketlogisticapp.TokenManager.TokensManager
import com.example.pocketlogisticapp.api.RetrofitClient
import com.example.pocketlogisticapp.dashbords.Customer
import com.example.pocketlogisticapp.model.LoginRequest
import com.example.pocketlogisticapp.model.LoginResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CustomerLogin : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_login)

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE)

        // Find views by ID
        val emailInput = findViewById<EditText>(R.id.editTextEmail)
        val passwordInput = findViewById<EditText>(R.id.editTextPassword)
        val loginButton = findViewById<Button>(R.id.buttonLogin)
        val signUpTextView = findViewById<TextView>(R.id.textViewSignUp)


        // Handle login click
        loginButton.setOnClickListener {
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()

            // Check if email and password are not empty
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter both email and password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Create the LoginRequest object
            val loginRequest = LoginRequest(email, password)

            // Make the login API call
            RetrofitClient.instance.loginUser(loginRequest)
                .enqueue(object : Callback<LoginResponse> {
                    override fun onResponse(
                        call: Call<LoginResponse>,
                        response: Response<LoginResponse>
                    ) {
                        if (response.isSuccessful && response.body() != null) {
                            val loginResponse = response.body()

                            Log.d("LOGIN_RESPONSE", "Body: ${loginResponse?.token}") // Logs just the body
                            val token = loginResponse?.token

                            // Store the token in SharedPreferences using TokenManager
                            if (token != null) {
                                TokensManager.storeToken(this@CustomerLogin,token, "Customer")
                            }


                            // Show a success message and navigate to the Customer dashboard
                            Toast.makeText(this@CustomerLogin, "Login successful!", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this@CustomerLogin, Customer::class.java))
                            finish()
                        } else {
                            // Handle login failure with response error message
                            val errorMessage = response.message() ?: "Unknown error"
                            Toast.makeText(this@CustomerLogin, "Login failed: $errorMessage", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                        // Handle failure (e.g., no internet, server issue)
                        Toast.makeText(this@CustomerLogin, "Login failed: ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
        }

        // Handle SignUp redirection
        signUpTextView.setOnClickListener {
            val url = "https://pocketindia.shop/signup" // Replace with your sign-up URL
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }
    }
}
