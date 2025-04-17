package com.example.pocketlogisticapp.TokenManager

import android.content.Context
import android.content.SharedPreferences

object TokensManager {

    private const val PREF_NAME = "MyAppPrefs"
    private const val TOKEN_KEY = "token"
    private const val ROLE_KEY = "role"

    // Get SharedPreferences instance
    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    // Store new token & role (clear previous role tokens)
    fun storeToken(context: Context, token: String, role: String) {
        val editor = getPrefs(context).edit()
        editor.putString(TOKEN_KEY, token)
        editor.putString(ROLE_KEY, role)
        editor.apply()
    }

    // Get saved token (regardless of role)
    fun getToken(context: Context): String? {
        return getPrefs(context).getString(TOKEN_KEY, null)
    }

    // Get saved role
    fun getCurrentRole(context: Context): String? {
        return getPrefs(context).getString(ROLE_KEY, null)
    }

    // Clear everything (logout)
    fun clearTokens(context: Context) {
        val editor = getPrefs(context).edit()
        editor.clear()
        editor.apply()
    }
}
