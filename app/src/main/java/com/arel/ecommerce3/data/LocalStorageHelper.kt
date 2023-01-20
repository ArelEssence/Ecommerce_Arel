package com.arel.ecommerce3.data

import android.content.Context
import android.content.SharedPreferences
import android.provider.ContactsContract.CommonDataKinds.Email

class LocalStorageHelper {

    var pref: SharedPreferences? = null
    var editor: SharedPreferences.Editor? = null
    var context: Context? = null

    var privateMode = 0
    private val prefName = "Local Storage"

    constructor(_context: Context) {
        this.context?.getSharedPreferences(prefName, privateMode)
        editor = pref!!.edit()
    }
    fun createLocalStorage(_dataLocal1: String, _dataLocal2: String){
        editor?.putString(_dataLocal1, _dataLocal1)
        editor?.putString(_dataLocal2, _dataLocal2)
        editor!!.commit()
    }

    fun getDataLocalStorage(): HashMap<String, String?>? {
        val dataLocalStorage = HashMap<String, String?>()
        val dataLocal1 = "Email"
        val dataLocal2 = "Password"

        dataLocalStorage[dataLocal1] = pref!!.getString(dataLocal1, "null")
        dataLocalStorage[dataLocal2] = pref!!.getString(dataLocal2, "null")
        return dataLocalStorage
    }


}