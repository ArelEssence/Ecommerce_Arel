package com.arel.ecommerce3

import android.content.Context
import android.content.Intent
import android.content.LocusId
import android.content.SharedPreferences
import android.media.session.MediaSession.Token

class SessionLogin(var context: Context) {
    var pref: SharedPreferences
    var editor: SharedPreferences.Editor
    var PRIVATE_MODE = 0

    fun createLoginSession(access_token: String?, refresh_token: String, id: String, name: String, email: String, phone: String,
    gender: String, image: String) {
        editor.putBoolean(IS_LOGIN, true)
        editor.putString(TOKEN, access_token)
        editor.putString(REFRESH_TOKEN, refresh_token)
        editor.putString(ID, id)
        editor.putString(NAME, name)
        editor.putString(EMAIL, email)
        editor.putString(PHONE, phone)
        editor.putString(GENDER, gender)
        if (image != null){
            editor.putString((IMAGE), image)
        }
        editor.commit()
    }

    fun logoutUser() {
        editor.clear()
        editor.commit()
        val intent = Intent(context, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }

    fun isLoggedIn(): Boolean = pref.getBoolean(IS_LOGIN, false)

    companion object {
        private const val PREF_NAME = "AbsensiPref"
        private const val IS_LOGIN = "IsLoggedIn"
        const val TOKEN = "access_token"
        const val REFRESH_TOKEN = "refresh_token"
        const val ID = "id"
        const val NAME = "name"
        const val EMAIL = "email"
        const val PHONE = "phone"
        const val GENDER = "gender"
        const val IMAGE = "image"
        const val BASE_URL = "https://portlan.id/training_android/public/"
    }

    init {
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        editor = pref.edit()
    }

    fun clear(){
        editor.clear().apply()
    }

    fun getPreference(key: String) : String? {
        return  pref.getString(key, null)
    }

    fun clearToken(){
        editor.remove(TOKEN)
        editor.remove(REFRESH_TOKEN)
        editor.apply()
    }

    fun putNewToken(access_token : String, refresh_token : String ){
        editor.apply{
            putString(TOKEN, access_token)
            putString(REFRESH_TOKEN, refresh_token)
        }
    }
}

