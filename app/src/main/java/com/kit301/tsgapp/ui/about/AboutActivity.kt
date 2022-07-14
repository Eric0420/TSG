package com.kit301.tsgapp.ui.about

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kit301.tsgapp.DrawerBaseActivity
import com.kit301.tsgapp.databinding.ActivityAboutBinding
import com.kit301.tsgapp.databinding.ActivitySettingsBinding
import com.kit301.tsgapp.databinding.ActivityHomepageBinding
import java.util.*

class AboutActivity : DrawerBaseActivity() {

    private lateinit var ui : ActivityAboutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ui = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(ui.root)
        setActionbarTitle()

    }

    private fun setActionbarTitle() {
        val sharedPreferences = getSharedPreferences("Settings", Activity.MODE_PRIVATE)
        val currentLanguageSetting = sharedPreferences.getString("My_Lang", "")
        if (currentLanguageSetting == "en" || currentLanguageSetting == ""){
            allocateActivityTitle("About")
        }
        else if (currentLanguageSetting == "zh"){
            allocateActivityTitle("关于")
        }
    }

}