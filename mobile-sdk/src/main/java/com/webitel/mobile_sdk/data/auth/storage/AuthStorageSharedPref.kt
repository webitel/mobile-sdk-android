package com.webitel.mobile_sdk.data.auth.storage

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.webitel.mobile_sdk.data.auth.AccessToken

internal class AuthStorageSharedPref(context: Context): AuthStorage {
    private val STORE_KEY_ACCESS_TOKEN_DATA = "user_token"
    private val SHARED_PREFS = "webitel_portal"

    private var gson: Gson = Gson()
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        SHARED_PREFS,
        Context.MODE_PRIVATE
    )
    private val editor: SharedPreferences.Editor = sharedPreferences.edit()

    override fun getAccessToken(): AccessToken? {
        val json =  sharedPreferences.getString(STORE_KEY_ACCESS_TOKEN_DATA, null)
        if (!json.isNullOrEmpty()) {
            return gson.fromJson(json, AccessToken::class.java)
        }
        return null
    }

    override fun saveAccessToken(token: AccessToken) {
        editor.putString(STORE_KEY_ACCESS_TOKEN_DATA, gson.toJson(token)).commit()
    }

    override fun clear() {
        editor.putString(STORE_KEY_ACCESS_TOKEN_DATA, null).commit()
    }
}