package com.example.pocketlogisticapp.fragments.AdminFragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.pocketlogisticapp.R
import com.example.pocketlogisticapp.TokenManager.TokensManager
import org.json.JSONObject

class createProductFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_create_product, container, false)

        val titleEditText = view.findViewById<EditText>(R.id.titleEditText)
        val priceEditText = view.findViewById<EditText>(R.id.priceEditText)
        val stockEditText = view.findViewById<EditText>(R.id.stockEditText)
        val imageUrlEditText = view.findViewById<EditText>(R.id.imageUrlEditText)
        val submitButton = view.findViewById<Button>(R.id.submitButton)

        submitButton.setOnClickListener {
            val title = titleEditText.text.toString().trim()
            val price = priceEditText.text.toString().toDoubleOrNull()
            val stock = stockEditText.text.toString().toIntOrNull()
            val imageUrl = imageUrlEditText.text.toString().trim()

            if (title.isEmpty() || price == null || stock == null || imageUrl.isEmpty()) {
                Toast.makeText(context, "Please fill all fields correctly", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val productData = JSONObject()
            productData.put("title", title)
            productData.put("price", price)
            productData.put("stock", stock)
            productData.put("duration", 1)
            productData.put("unit","years")
            productData.put("image", imageUrl)

            // Send API request
            val token = TokensManager.getToken(requireContext())
            val url = "https://api.pocketindia.shop/api/v1/admin/create/product"
            val requestQueue = Volley.newRequestQueue(requireContext())

            val request = object : JsonObjectRequest(Request.Method.POST, url, productData,
                { response ->
                    Toast.makeText(context, "Product Created Successfully", Toast.LENGTH_SHORT).show()
                },
                { error ->
                    val message = error.networkResponse?.let {
                        String(it.data)
                    } ?: error.message

                    Toast.makeText(context, "Error: $message", Toast.LENGTH_SHORT).show()
                }
            ) {
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Content-Type"] = "application/json"
                    headers["Authorization"] = "$token"  // << use your token dynamically
                    return headers
                }
            }

            requestQueue.add(request)
        }


        return view
    }
}
