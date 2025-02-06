package com.pascal.ecommercecompose.data.prefs

import android.content.Context
import androidx.core.content.edit
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

object PreferencesLogin {

    private const val PREFS_NAME = "login_prefs"
    private const val IS_LOGIN = "isLogin"
    private const val RESPONSE_LOGIN = "response_login"

    fun setLoginResponse(context: Context, value: String?) {
        val jsonString = Json.encodeToString(value)
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit {
            putString(RESPONSE_LOGIN, jsonString)
            commit()
        }
    }

    fun getLoginResponse(context: Context): String? {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val jsonString =  sharedPreferences.getString(RESPONSE_LOGIN, "")
        return jsonString.let { Json.decodeFromString(it ?: "") }
    }

    fun setIsLogin(context: Context, isLogin: Boolean) {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit {
            putBoolean(IS_LOGIN, isLogin)
            commit()
        }
    }

    fun getIsLogin(context: Context): Boolean {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean(IS_LOGIN, false)
    }

    fun deleteLoginData(context: Context) {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit {
            clear()
            commit()
        }
    }
}

