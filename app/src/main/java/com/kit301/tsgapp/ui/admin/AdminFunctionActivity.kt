package com.kit301.tsgapp.ui.admin

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.kit301.tsgapp.DrawerBaseActivity
import com.kit301.tsgapp.databinding.ActivityAdminFunctionBinding
import com.kit301.tsgapp.ui.notification.SendNotification


class AdminFunctionActivity : DrawerBaseActivity() {

    private lateinit var ui: ActivityAdminFunctionBinding

    //Firebase Auth
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ui = ActivityAdminFunctionBinding.inflate(layoutInflater)
        setContentView(ui.root)
        setActionbarTitle()

        //Init Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()

        //Handle the action of logout button
        ui.logoutBtn.setOnClickListener{
            firebaseAuth.signOut()
            checkUser()
        }

        //Set the action of the pushNotification button
        ui.pushNotificationBtn.setOnClickListener{
            val intent = Intent(this, SendNotification::class.java)
            startActivity(intent)
        }

    }

    private fun checkUser() {
        //Check the user login or not
        val firebaseUser = firebaseAuth.currentUser

        if (firebaseUser != null){
            //User not null, the user already login, get user information
            val email = firebaseUser.email

            //Display the email address in text view
            ui.emailTv.text = email

        }
        else{
            //User is null, the user is not login, go to the Admin Login activity
            startActivity(Intent(this, AdminActivity::class.java))
            finish()
        }
    }


    private fun setActionbarTitle() {
        val sharedPreferences = getSharedPreferences("Settings", Activity.MODE_PRIVATE)
        val currentLanguageSetting = sharedPreferences.getString("My_Lang", "")
        if (currentLanguageSetting == "en" || currentLanguageSetting == ""){
            allocateActivityTitle("Admin Setting Page")
        }
        else if (currentLanguageSetting == "zh"){
            allocateActivityTitle("管理员设置页面")
        }
    }

}