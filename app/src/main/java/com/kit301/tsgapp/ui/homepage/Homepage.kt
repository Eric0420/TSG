package com.kit301.tsgapp.ui.homepage

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.kit301.tsgapp.DataNotFound
import com.kit301.tsgapp.DrawerBaseActivity
import com.kit301.tsgapp.databinding.ActivityHomepageBinding
import com.kit301.tsgapp.ProductDetails
import com.kit301.tsgapp.ui.test.FIREBASE_TAG
import com.kit301.tsgapp.ui.test.Test
import java.util.*

private const val CAMERA_REQUEST_CODE = 101

const val Scan_Text = "Scan_Text"  //This variable use to store the scanned barcode number


class Homepage : DrawerBaseActivity() {

    private lateinit var ui : ActivityHomepageBinding

    private lateinit var codeScanner: CodeScanner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadLocate()  //Set the language
        ui = ActivityHomepageBinding.inflate(layoutInflater)
        setContentView(ui.root)

        setActionbarTitle()

        setupPermissions()
        codeScanner()

    }

    private fun setActionbarTitle() {
        var currentLanguageSetting = Locale.getDefault().getLanguage()

        if (currentLanguageSetting == "en" || currentLanguageSetting == ""){
            allocateActivityTitle("Home")
        }
        else if (currentLanguageSetting == "zh"){
            allocateActivityTitle("主页")
        }
    }

    //This is for apply the translation function in the app
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




    private fun codeScanner(){
        val scannerView = ui.scannerView
        val tvTextView = ui.tvTextview

        codeScanner = CodeScanner(this,scannerView)  //Initial code scanner and insert the scanner view from XML file

        codeScanner.apply {
            camera = CodeScanner.CAMERA_BACK  //Define which side of camera we use
            formats = CodeScanner.ALL_FORMATS  //Define the code scanner support all the code format

            autoFocusMode = AutoFocusMode.SAFE  //Try to autofocus at fixed intervals
            scanMode = ScanMode.CONTINUOUS  //Continuously try to scan and find the barcodes
            isAutoFocusEnabled = true  //Set the autofocus initially to true
            isFlashEnabled = false  //Set the flash off when you open the scan view

            decodeCallback = DecodeCallback {
                runOnUiThread{  //it refers to the result
                    tvTextView.text = it.text  //Set the text to the text that it has successfully decoded from the barcode

                }

                if (it.text != null){
                    val intent = Intent(this@Homepage, ProductDetails::class.java)
                    //val intent = Intent(this@Homepage, Test::class.java)
                    intent.putExtra(Scan_Text,it.text.toString())
                    startActivity(intent)
                }


            }

            errorCallback = ErrorCallback {
                runOnUiThread{
                    Log.e("Main","Camera initialization error: ${it.message}")
                }
            }
        }

        scannerView.setOnClickListener{  //This just in case decide to set the ScanMode to anything else that is not the continuous
            codeScanner.startPreview()  //Tell the program that you want to start scanning the new code
        }

    }


    override fun onResume() {
        //When you leave and come back to the app, it will try to fetch a new code, and also in case the ScanMode is not set to continuous
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        //Help to avoid using the resource and avoid memory leaks
        codeScanner.releaseResources()
        super.onPause()
    }

    //For the device run on API level above 23, create run time permission, otherwise the camera will not show up on the screen
    private fun setupPermissions(){
        //Check the permission
        val permission = ContextCompat.checkSelfPermission(this,
            android.Manifest.permission.CAMERA)

        //Call the makeRequest function of the user do not have the camera permission
        if(permission != PackageManager.PERMISSION_GRANTED){
            makeRequest()
        }
    }

    private fun makeRequest(){
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA),
            CAMERA_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode){
            CAMERA_REQUEST_CODE -> {  //When requestCode = CAMERA_REQUEST_CODE
                if(grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this, "You need the camera permission", Toast.LENGTH_SHORT).show()
                }
                else{  //If it is a successful request, do not need to do anything
                    //successful
                }

            }
        }
    }


}