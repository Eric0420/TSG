package com.kit301.tsgapp.ui.search

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import com.kit301.tsgapp.DrawerBaseActivity
import com.kit301.tsgapp.databinding.ActivitySearchProductBinding
import com.kit301.tsgapp.ui.settings.SettingsActivity
import java.util.*

class SearchProduct : DrawerBaseActivity() {

    private lateinit var ui : ActivitySearchProductBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ui = ActivitySearchProductBinding.inflate(layoutInflater)
        setContentView(ui.root)
        setActionbarTitle()

    }

    private fun setActionbarTitle() {
        val sharedPreferences = getSharedPreferences("Settings", Activity.MODE_PRIVATE)
        val currentLanguageSetting = sharedPreferences.getString("My_Lang", "")

        if (currentLanguageSetting == "en" || currentLanguageSetting == ""){
            allocateActivityTitle("Search Product")
        }
        else if (currentLanguageSetting == "zh"){
            allocateActivityTitle("搜索商品")
        }
    }



}