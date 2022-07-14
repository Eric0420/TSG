package com.kit301.tsgapp.ui.settings

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.kit301.tsgapp.DrawerBaseActivity
import com.kit301.tsgapp.databinding.ActivityHomepageBinding
import com.kit301.tsgapp.databinding.ActivitySettingsBinding
import java.util.*


class SettingsActivity : DrawerBaseActivity() {

    private lateinit var ui : ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadLocate()  //Set the language
        ui = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(ui.root)
        setActionbarTitle()  //Set the actionbar title based on the current language setting

        ui.btnChangeLanguage.setOnClickListener{
            showChangeLanguageDialog()
        }
    }

    private fun setActionbarTitle() {
        var currentLanguageSetting = Locale.getDefault().getLanguage()
        if (currentLanguageSetting == "en" || currentLanguageSetting == ""){
            allocateActivityTitle("Settings")
        }
        else if (currentLanguageSetting == "zh"){
            allocateActivityTitle("设置")
        }
    }

    private fun showChangeLanguageDialog() {
        val listItems = arrayOf("English", "中文")

        val mBuilder = AlertDialog.Builder(this)
        mBuilder.setTitle("Please Select Language")
        mBuilder.setSingleChoiceItems(listItems,-1){ dialog, result->
            if (result == 0){
                setLocate("en")
                recreate()
            }
            else if (result == 1){
                setLocate("zh")
                recreate()
            }

            //Dismiss alert dialog when language selected
            dialog.dismiss()
        }

        val mDialog = mBuilder.create()

        //Show alert dialog
        mDialog.show()

    }

    private fun setLocate(Lang: String) {
        val locale = Locale(Lang)
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        baseContext.resources.updateConfiguration(config,baseContext.resources.displayMetrics)

        //Save data to shared preferences
        val editor = getSharedPreferences("Settings", Context.MODE_PRIVATE).edit()
        editor.putString("My_Lang",Lang)
        editor.apply()

    }

    //Load language saved in shared preferences
    private fun loadLocate(){
        val sharedPreferences = getSharedPreferences("Settings", Activity.MODE_PRIVATE)
        val language = sharedPreferences.getString("My_Lang", "")
        setLocate(language!!)

    }

}
