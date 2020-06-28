package com.example.elist.settings

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import com.example.elist.R
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity() {

    private var mySwitch: Switch? = null

    lateinit var sharedPref: SharedPref

    override fun onCreate(savedInstanceState: Bundle?) {
        sharedPref = SharedPref(this)

        if (sharedPref.loadNightModeState()) {
            setTheme(R.style.DarkTheme)

        } else setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_settings)
        mySwitch = findViewById<View>(R.id.my_switch) as Switch
        if (sharedPref.loadNightModeState()) {
            mySwitch!!.isChecked = true
        }

        mySwitch!!.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                sharedPref.setNigthModeState(true)
                restartApp()
            } else {
                sharedPref.setNigthModeState(false)
                restartApp()
            }
        }

        setSupportActionBar(settings_toolbar)
        title = "Настройки"
    }

    private fun restartApp() {
        val s = Intent(applicationContext, SettingsActivity::class.java)
 //       val m = Intent(applicationContext, MainActivity::class.java)
        startActivity(s)
 //       startActivity(m)
        finish()
    }
}