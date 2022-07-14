package com.kit301.tsgapp.ui.notification

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kit301.tsgapp.DrawerBaseActivity
import com.kit301.tsgapp.databinding.ActivityHomepageBinding
import com.kit301.tsgapp.databinding.ActivityNotificationBinding

class NotificationActivity : DrawerBaseActivity() {

    private lateinit var ui : ActivityNotificationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ui = ActivityNotificationBinding.inflate(layoutInflater)
        setContentView(ui.root)
        setActionbarTitle()


    }

    private fun setActionbarTitle() {
        val sharedPreferences = getSharedPreferences("Settings", Activity.MODE_PRIVATE)
        val currentLanguageSetting = sharedPreferences.getString("My_Lang", "")
        if (currentLanguageSetting == "en" || currentLanguageSetting == ""){
            allocateActivityTitle("Notification")
        }
        else if (currentLanguageSetting == "zh"){
            allocateActivityTitle("通知")
        }
    }
}