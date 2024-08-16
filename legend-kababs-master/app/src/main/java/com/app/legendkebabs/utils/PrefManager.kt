package com.app.legendkebabs.di

import android.content.SharedPreferences
import com.google.gson.Gson
import com.app.legendkebabs.constants.WebServiceAppConstants
import javax.inject.Inject

class PrefManager @Inject constructor(
    private val preferences: SharedPreferences,
    private val gson: Gson
) {
    //Keys
    private val PREF_KEY_TOKEN = "token"
    private val PREF_KEY_REFRESH_TOKEN = "refresh_token"
    private val PREF_KEY_ENTITY = "entity"
    private val PREF_KEY_USERNAME = "username"
    private val PREF_KEY_PASSWORD = "password"
    private val PREF_KEY_IS_LOGGED_IN = "is_logged_in"


    //Methods
    fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return preferences.getBoolean(key, defaultValue)
    }

    fun putBoolean(key: String, value: Boolean) {
        try {
            println("$key $value")
            preferences.edit().putBoolean(key, value).apply()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    fun getString(key: String, defaultValue: String): String {
        return preferences.getString(key, defaultValue)!!
    }

    fun putString(key: String, value: String) {
        try {
            println("$key $value")
            preferences.edit().putString(key, value).apply()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

    }

    fun getIsLoggedIn() : Boolean {
        return getBoolean(PREF_KEY_IS_LOGGED_IN, false)
    }

    fun putIsLoggedIn(isLoggedIn: Boolean) {
        putBoolean(PREF_KEY_IS_LOGGED_IN, isLoggedIn)
    }

    fun getToken() : String {
        return getString(PREF_KEY_TOKEN, "")
    }

    fun putToken(token: String) {
        putString(PREF_KEY_TOKEN, token)
    }

    fun getRefreshToken() : String {
        return getString(PREF_KEY_REFRESH_TOKEN, "")
    }

    fun putRefreshToken(token: String) {
        putString(PREF_KEY_REFRESH_TOKEN, token)
    }

    fun putCredentials(credentials: Map<String, String>) {
        putString(PREF_KEY_USERNAME, credentials["username"]!!)
        putString(PREF_KEY_PASSWORD, credentials["password"]!!)
    }

    fun clearAllPreferences() {
//        preferences.edit().clear().apply()
        preferences.edit()
            .remove(PREF_KEY_TOKEN)
            .remove(PREF_KEY_IS_LOGGED_IN)
            .remove(PREF_KEY_ENTITY)
            .apply()
    }
}