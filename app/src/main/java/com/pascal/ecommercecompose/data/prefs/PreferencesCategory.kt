package com.pascal.ecommercecompose.data.prefs

import android.content.Context
import androidx.core.content.edit
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

object PreferencesCategory {

    private const val PREFS_NAME = "category_prefs"
    private const val RESPONSE_CATEGORY = "response_category"

    fun setCategoryResponse(context: Context, value: List<String>?) {
        val jsonString = Json.encodeToString(value)
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit {
            putString(RESPONSE_CATEGORY, jsonString)
            commit()
        }
    }

    fun getCategoryResponse(context: Context): List<String>? {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val jsonString =  sharedPreferences.getString(RESPONSE_CATEGORY, "")
        return jsonString.let { Json.decodeFromString(it ?: "") }
    }

    fun deleteCategoryData(context: Context) {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit {
            clear()
            commit()
        }
    }
}
