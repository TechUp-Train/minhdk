package com.example.aigenerator.data

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class SharedPrefImpl(
    private val context: Context
): SharedPref {

    companion object {
        private const val PREF_NAME = "image_generator"

        // KEYS
        private const val KEY_DEVICE_ID = "KEY_DEVICE_ID"
    }

    private var prefs: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    private fun putString(key: String, value: String) {
        prefs.edit { putString(key, value) }
    }

    private fun getString(key: String, default: String = ""): String {
        return prefs.getString(key, default) ?: default
    }

    private fun putBoolean(key: String, value: Boolean) {
        prefs.edit { putBoolean(key, value) }
    }

    private fun getBoolean(key: String, default: Boolean = false): Boolean {
        return prefs.getBoolean(key, default)
    }

    private fun putInt(key: String, value: Int) {
        prefs.edit { putInt(key, value) }
    }

    private fun getInt(key: String, default: Int = 0): Int {
        return prefs.getInt(key, default)
    }

    private fun clear() {
        prefs.edit { clear() }
    }

    override var deviceId: String
        get() = getString(KEY_DEVICE_ID)
        set(value) = putString(KEY_DEVICE_ID, value)

}