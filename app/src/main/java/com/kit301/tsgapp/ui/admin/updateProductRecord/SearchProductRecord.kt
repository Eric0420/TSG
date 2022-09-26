package com.kit301.tsgapp.ui.admin.updateProductRecord

import android.R
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
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
import com.kit301.tsgapp.DrawerBaseActivity
import com.kit301.tsgapp.databinding.ActivitySearchProductRecordBinding

private const val CAMERA_REQUEST_CODE = 101

const val Search_Result = "Search_Result"  //This variable use to store the scanned barcode number

const val Current_Language_Selection = "Current_Language_Selection"


class SearchProductRecord : DrawerBaseActivity() {
    private lateinit var ui: ActivitySearchProductRecordBinding
    private lateinit var codeScanner: CodeScanner

    var LanguageSelection = "en"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ui = ActivitySearchProductRecordBinding.inflate(layoutInflater)
        setContentView(ui.root)
        setActionbarTitle()

        //Set the camera permission
        setupPermissions()

        //Initiate the Dropdown menu
        setLanguageMenuOption()

        //Use camera to scan barcode and handle the scanning result
        codeScanner()


    }


    private fun setLanguageMenuOption() {
        val languageOption = arrayOf("English", "中文")

        ui.databaseOption.adapter = ArrayAdapter<String>(this, R.layout.simple_list_item_1, languageOption)

        ui.databaseOption.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                //ui.textView6.text = languageOption.get(position)

                if(position == 0){
                    //ui.textView3.text = "en"
                    //recreate()
                    LanguageSelection = "en"
                }

                if(position == 1){
                    //ui.textView3.text = "zh"
                    //recreate()
                    LanguageSelection = "zh"
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                //ui.textView3.text = "Please select your language option"
            }
        }


    }



    private fun codeScanner(){
        val scannerView = ui.barcodeScanner

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
                    //ui.textViewTest.text = it.text  //Set the text to the text that it has successfully decoded from the barcode

                }

                if (it.text != null){
                    val intent = Intent(this@SearchProductRecord, UpdateProductDetails::class.java)
                    intent.putExtra(Search_Result, it.text.toString())
                    intent.putExtra(Current_Language_Selection, LanguageSelection)
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

    private fun setActionbarTitle() {
        val sharedPreferences = getSharedPreferences("Settings", Activity.MODE_PRIVATE)
        val currentLanguageSetting = sharedPreferences.getString("My_Lang", "")
        if (currentLanguageSetting == "en" || currentLanguageSetting == ""){
            allocateActivityTitle("Search Product Record")
        }
        else if (currentLanguageSetting == "zh"){
            allocateActivityTitle("搜索产品记录")
        }
    }

}