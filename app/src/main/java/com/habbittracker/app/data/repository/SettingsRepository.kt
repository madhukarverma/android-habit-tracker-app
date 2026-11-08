package com.habbittracker.app.data.repository

import android.content.Context
import android.content.SharedPreferences

class SettingsRepository(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("habit_tracker_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_USER_NAME = "user_name"
        private const val DEFAULT_NAME = "Student"
    }

    fun getUserName(): String {
        return prefs.getString(KEY_USER_NAME, DEFAULT_NAME) ?: DEFAULT_NAME
    }

    fun setUserName(name: String) {
        prefs.edit().putString(KEY_USER_NAME, name).apply()
    }
}
