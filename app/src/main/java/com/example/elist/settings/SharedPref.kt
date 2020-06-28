package com.example.elist.settings

import android.content.Context
import android.content.SharedPreferences

class SharedPref (context: Context) {

    var mySharedPref: SharedPreferences = context.getSharedPreferences("filename", Context.MODE_PRIVATE)

    fun setNigthModeState(state: Boolean?) {
        val editor = mySharedPref.edit()
        editor.putBoolean("NightMode", state!!)
        editor.apply()
    }

    fun loadNightModeState(): Boolean {
        return mySharedPref.getBoolean("NightMode", false)
    }

}