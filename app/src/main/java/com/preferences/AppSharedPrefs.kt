package com.preferences

import android.content.Context
import com.application.AndroidSensorEngine.Companion.globalAppContext

class AppSharedPrefs {

    fun saveCondition(keyName: String, condition: Boolean) {
        val sharedPref = globalAppContext.getSharedPreferences("UserPreferences", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putBoolean(keyName, condition)
            apply()
        }
    }

    fun getCondition(keyName: String): Boolean {
        val sharedPref = globalAppContext.getSharedPreferences("UserPreferences", Context.MODE_PRIVATE)
        return sharedPref.getBoolean(keyName, false) // Default is 'false' if not found
    }

}