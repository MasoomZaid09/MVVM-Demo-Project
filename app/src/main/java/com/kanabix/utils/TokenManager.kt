package com.kanabix.utils

import android.content.Context
import android.content.SharedPreferences
import com.kanabix.api.Constants.PREFS_TOKEN_FILE
import com.kanabix.api.Constants.USER_TOKEN

class TokenManager (var  context: Context) {
    private var prefs: SharedPreferences =
        context.getSharedPreferences(PREFS_TOKEN_FILE, Context.MODE_PRIVATE)

    fun saveToken(token: String) {
        val editor = prefs.edit()
        editor.putString(USER_TOKEN, token)
        editor.apply()
    }

    fun getToken(): String? {
        return prefs.getString(USER_TOKEN, null)
    }
}