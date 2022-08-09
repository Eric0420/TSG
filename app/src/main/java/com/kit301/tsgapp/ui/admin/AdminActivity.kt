@file:Suppress("DEPRECATION")

package com.kit301.tsgapp.ui.admin

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.kit301.tsgapp.DrawerBaseActivity
import com.kit301.tsgapp.databinding.ActivityAdminBinding


class AdminActivity : DrawerBaseActivity() {

    private lateinit var ui : ActivityAdminBinding

    //Progress Dialog
    private lateinit var progressDialog: ProgressDialog

    //Firebase Auth
    private lateinit var firebaseAuth: FirebaseAuth
    private var email = ""
    private var password = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ui = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(ui.root)
        setActionbarTitle()

        //Config progress dislog
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please wait")
        progressDialog.setMessage("Logging In...")
        progressDialog.setCanceledOnTouchOutside(false)

        //Init FirebaseAuth
        firebaseAuth = FirebaseAuth.getInstance()

        //Check the user login status
        checkUser()

        //Handle the action of the login button
        ui.loginBtn.setOnClickListener{
            //Before login, validate the username and password, then allow user to login
            validateData()
        }


    }

    private fun validateData() {
        //Get username and password
        email = ui.emailEt.text.toString().trim()
        password = ui.passwordEt.text.toString().trim()

        //Validate username and password
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            //Invalid email format
            ui.emailEt.error = "Invalid email format"
        }
        else if (TextUtils.isEmpty(password)){
            //If user do not entered the password
            ui.passwordEt.error = "Please enter the password"
        }
        else{
            //The validation is success, allow the user to login
            firebaseLogin()

        }

    }

    private fun firebaseLogin() {
        //Show progress dialog
        progressDialog.show()

        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                //Login success
                progressDialog.dismiss()

                //Get user information
                val firebaseUser = firebaseAuth.currentUser
                val email = firebaseUser!!.email
                Toast.makeText(this,"LoggedIn as $email", Toast.LENGTH_SHORT).show()

                //Open the Admin Setting Page
                startActivity(Intent(this, AdminFunctionActivity::class.java))
                finish()
            }
            .addOnFailureListener{ e->
                //Login failed
                progressDialog.dismiss()
                Toast.makeText(this,"Login failed due to ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun checkUser() {
        //if user is already logged in, set the next action
        //get current user
        val firebaseUser = firebaseAuth.currentUser

        if (firebaseUser != null){
            //user already logged in, go to the Admin Setting Page
            startActivity(Intent(this, AdminFunctionActivity::class.java))
            finish()

        }
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