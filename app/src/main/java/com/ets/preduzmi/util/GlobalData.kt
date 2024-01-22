package com.ets.preduzmi.util

import android.content.Context

object GlobalData {
    private var _token: String? = null

    fun loadTokenFromSharedPrefs(content: Context) {
        val sharedPrefs = content.getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        _token = sharedPrefs.getString("token", null)
    }

    fun saveToken(content: Context, value: String?) {
        _token = value

        val sharedPrefs = content.getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        val editor = sharedPrefs.edit()

        if (value == null) {
            editor.remove("token")
        } else {
            editor.putString("token", value)
        }
        editor.apply()
    }

    fun setToken(value: String?) {
        _token = value
    }

    fun getToken(): String? {
        return _token
    }

    val API_PREFIX = "http://84.247.177.105:3000/api/"

    val BUSINESS_TYPES = listOf(
        "Edukacija",
        "Ekologija",
        "Usluge",
        "IT",
        "Restoran",
        "Turizam",
        "Zdravlje",
        "Drugo..."
    )
    val BUSINESS_LEGAL_TYPES = listOf(
        "D.O.O.",
        "A.D.",
        "K.O.",
        "O.D."
    )
}
