package com.kit301.tsgapp.ui.history

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kit301.tsgapp.DrawerBaseActivity
import com.kit301.tsgapp.databinding.ActivityHistoryBinding
import com.kit301.tsgapp.databinding.ActivityHomepageBinding

class HistoryActivity : DrawerBaseActivity() {

    private lateinit var ui : ActivityHistoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ui = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(ui.root)
        setActionbarTitle()
    }

    private fun setActionbarTitle() {
        val sharedPreferences = getSharedPreferences("Settings", Activity.MODE_PRIVATE)
        val currentLanguageSetting = sharedPreferences.getString("My_Lang", "")
        if (currentLanguageSetting == "en" || currentLanguageSetting == ""){
            allocateActivityTitle("History")
        }
        else if (currentLanguageSetting == "zh"){
            allocateActivityTitle("历史记录")
        }
    }
}