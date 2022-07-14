package com.kit301.tsgapp.ui.admin

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kit301.tsgapp.DrawerBaseActivity
import com.kit301.tsgapp.databinding.ActivityAdminBinding
import com.kit301.tsgapp.databinding.ActivityHomepageBinding

class AdminActivity : DrawerBaseActivity() {

    private lateinit var ui : ActivityAdminBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ui = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(ui.root)
        setActionbarTitle()


    }

    private fun setActionbarTitle() {
        val sharedPreferences = getSharedPreferences("Settings", Activity.MODE_PRIVATE)
        val currentLanguageSetting = sharedPreferences.getString("My_Lang", "")
        if (currentLanguageSetting == "en" || currentLanguageSetting == ""){
            allocateActivityTitle("Admin")
        }
        else if (currentLanguageSetting == "zh"){
            allocateActivityTitle("管理员页面")
        }
    }
}