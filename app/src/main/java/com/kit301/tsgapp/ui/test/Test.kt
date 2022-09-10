@file:Suppress("DEPRECATION")

package com.kit301.tsgapp.ui.test

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.kit301.tsgapp.*
import com.kit301.tsgapp.databinding.ActivityProductDetailsBinding
import com.kit301.tsgapp.databinding.ActivityTestBinding
import com.kit301.tsgapp.ui.homepage.Scan_Text
import com.kit301.tsgapp.ui.notification.SendNotification
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern


const val FIREBASE_TAG = "FirebaseLogging"   //Print things to the console for debugging database error
val db = Firebase.firestore
var isMyFavourite : Boolean = false  //This variable use to control the action of the Favourite button


class Test : DrawerBaseActivity() {
    private lateinit var ui : ActivityProductDetailsBinding

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ui = ActivityProductDetailsBinding.inflate(layoutInflater)
        setContentView(ui.root)
        setActionbarTitle()



        //Product1 Barcode
        //val barcodeNumber = "9323245004567"

        //Product2 Barcode
        //If you want to display the second product details on the product details page, manually common the first barcodeNumber and uncommon the second barcodeNumber
        val barcodeNumber = "9300701548285"

        readData(barcodeNumber)




    }



    private fun setActionbarTitle() {
        val sharedPreferences = getSharedPreferences("Settings", Activity.MODE_PRIVATE)
        val currentLanguageSetting = sharedPreferences.getString("My_Lang", "")
        if (currentLanguageSetting == "en" || currentLanguageSetting == ""){
            allocateActivityTitle("Test")
        }
        else if (currentLanguageSetting == "zh"){
            allocateActivityTitle("测试页面")
        }
    }

    private fun readData(barcodeNumber: String?) {


        var currentLanguage = getSharedPreferences("Settings", Activity.MODE_PRIVATE).getString("My_Lang", "")

        //This if statement is used to prevent error (If do not add this statement, when user do not change the language setting, the currentLanguage value will be null)
        if (currentLanguage == "en" || currentLanguage == ""){
            currentLanguage = "en"
        }



        var productsCollection = db.collection("$currentLanguage" + "Product")

        productsCollection
                .whereEqualTo("barcodeNumber","${barcodeNumber}")  //This is a Query for searching the document which barcodeNum is: "${barcodeNum}"
                .get()
                .addOnSuccessListener { result->
                    //If the searching result is empty (Do not have the product with the scanned barcode number)
                    if (result.isEmpty){
                        val intent = Intent(this, DataNotFound::class.java)
                        startActivity(intent)
                        Log.e(FIREBASE_TAG, "No product record in database")
                        finish()
                    }

                    //If the searching result is not empty (Have the product with the scanned barcode number)
                    else {
                        for (document in result) {
                            //Setup the User Interface
                            val product = document.toObject<Product>()
                            setupUI(product)

                            //Check whether or not the current product is in favourite list
                            checkFavouriteStatus(product)

                            //Set the action of the Favourite button
                            ui.btnFavourite.setOnClickListener {


                                //If the current product exist in favourite list
                                if (isMyFavourite) {
                                    ui.btnFavourite.backgroundTintList = ColorStateList.valueOf(Color.BLACK)
                                    isMyFavourite = false

                                    //Delete the favourite product when the button click
                                    deleteFavouriteProduct(barcodeNumber)
                                }

                                //If the current product not exist in favourite list
                                else {
                                    ui.btnFavourite.backgroundTintList = ColorStateList.valueOf(Color.RED)
                                    isMyFavourite = true

                                    //Add the favourite product when the button click
                                    addFavouriteProduct(barcodeNumber)
                                }
                            }  //This is the end of the favourite button action


                            //Share product image
                            ui.btnShare.setOnClickListener {

                            }

                        }
                    }

                }

                .addOnFailureListener{
                    Log.d(FIREBASE_TAG, "Error getting documents")
                    Toast.makeText(this,"Failed",Toast.LENGTH_SHORT).show()
                }


    }

    private fun addFavouriteProduct(barcodeNumber: String?) {

        val enProductsCollection = db.collection("enProduct")  //The English Language product database
        val zhProductsCollection = db.collection("zhProduct")  ////The Chinese Language product database
        val deviceID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID)

        //Add the English Version Data to the UserFavouriteProduct Database
        enProductsCollection
                .whereEqualTo("barcodeNumber","${barcodeNumber}")  //This is a Query for searching the document which barcodeNum is: "${barcodeNum}"
                .get()
                .addOnSuccessListener { result->
                    for (document in result) {
                        val product = document.toObject<Product>()

                        val productCollection = db.collection("UserFavouriteProduct")
                        productCollection.document(deviceID)
                                .collection("en")
                                .document("${product.Name}")
                                .set(product)
                                .addOnSuccessListener {
                                    //Display the text to tell the user that the action is success
                                    Toast.makeText(this, "Successfully added to you favourite list", Toast.LENGTH_SHORT).show()
                                    Log.d(FIREBASE_TAG, "Document created with product name: ${product.Name}")
                                }
                                .addOnFailureListener {
                                    Toast.makeText(this, "Failed to add to favourite list", Toast.LENGTH_SHORT).show()
                                    Log.e(FIREBASE_TAG, "Error writing document")
                                }

                    }
                }

        //Add the Chinese Version Data to the UserFavouriteProduct Database
        zhProductsCollection
                .whereEqualTo("barcodeNumber","${barcodeNumber}")  //This is a Query for searching the document which barcodeNum is: "${barcodeNum}"
                .get()
                .addOnSuccessListener { result->
                    for (document in result) {
                        val product = document.toObject<Product>()

                        val productCollection = db.collection("UserFavouriteProduct")
                        productCollection.document(deviceID)
                                .collection("zh")
                                .document("${product.Name}")
                                .set(product)
                                .addOnSuccessListener {
                                    //Display the text to tell the user that the action is success
                                    Toast.makeText(this, "Successfully added to you favourite list", Toast.LENGTH_SHORT).show()
                                    Log.d(FIREBASE_TAG, "Document created with product name: ${product.Name}")
                                }
                                .addOnFailureListener {
                                    Toast.makeText(this, "Failed to add to favourite list", Toast.LENGTH_SHORT).show()
                                    Log.e(FIREBASE_TAG, "Error writing document")
                                }

                    }
                }

    }

    private fun deleteFavouriteProduct(barcodeNumber: String?) {

        val enProductsCollection = db.collection("enProduct")  //The English Language product database
        val zhProductsCollection = db.collection("zhProduct")  //The Chinese Language product database
        val deviceID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID)

        //Delete the English Version Data from the UserFavouriteProduct Database
        enProductsCollection
                .whereEqualTo("barcodeNumber","${barcodeNumber}")  //This is a Query for searching the document which barcodeNum is: "${barcodeNum}"
                .get()
                .addOnSuccessListener { result->
                    for (document in result) {
                        val product = document.toObject<Product>()

                        val productCollection = db.collection("UserFavouriteProduct")
                        productCollection.document(deviceID)
                                .collection("en")
                                .document("${product.Name}")
                                .delete()
                                .addOnSuccessListener {
                                    Toast.makeText(this, "Successfully remove the product from your favourite list", Toast.LENGTH_SHORT).show()
                                    Log.d(FIREBASE_TAG, "Successfully deleted with product name: ${product.Name}")
                                }
                                .addOnFailureListener {
                                    Log.e(FIREBASE_TAG, "Error in deleting document")
                                }


                    }
                }

        //Delete the Chinese Version Data from the UserFavouriteProduct Database
        zhProductsCollection
                .whereEqualTo("barcodeNumber","${barcodeNumber}")  //This is a Query for searching the document which barcodeNum is: "${barcodeNum}"
                .get()
                .addOnSuccessListener { result->
                    for (document in result) {
                        val product = document.toObject<Product>()

                        val productCollection = db.collection("UserFavouriteProduct")
                        productCollection.document(deviceID)
                                .collection("zh")
                                .document("${product.Name}")
                                .delete()
                                .addOnSuccessListener {
                                    Toast.makeText(this, "Successfully remove the product from your favourite list", Toast.LENGTH_SHORT).show()
                                    Log.d(FIREBASE_TAG, "Successfully deleted with product name: ${product.Name}")
                                }
                                .addOnFailureListener {
                                    Log.e(FIREBASE_TAG, "Error in deleting document")
                                }


                    }
                }

    }




    private fun checkFavouriteStatus(product: Product) {

        //Get the Android Device ID for identifying different devices
        val deviceID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID)

        //Get the current language setting (Check which language is using)
        var currentLanguage = getSharedPreferences("Settings", Activity.MODE_PRIVATE).getString("My_Lang", "")

        //This if statement is used to prevent error (If do not add this statement, when user do not change the language setting, the currentLanguage value will be null)
        if (currentLanguage == "en" || currentLanguage == ""){
            currentLanguage = "en"
        }


        //Check whether or not the current product is in favourite list
        val checkFavouriteStatus = FirebaseFirestore.getInstance().collection("UserFavouriteProduct")
                .document(deviceID).collection("$currentLanguage")
                .document("${product.Name.toString()}")
        checkFavouriteStatus.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val document = task.result
                if (document != null) {
                    if (document.exists()) {
                        ui.btnFavourite.backgroundTintList = ColorStateList.valueOf(Color.RED)
                        isMyFavourite = true
                    } else {
                        isMyFavourite = false
                    }
                }
            } else {
                Log.d("TAG", "Error: ", task.exception)
            }
        }
    }

    private fun setupUI(product: Product) {
        //Setup the User Interface
        ui.productName.text = product.Name
        ui.productABV.text = product.ABV
        ui.productYear.text = product.YearOfRelease.toString()
        ui.productPrice.text = product.AverageSalesPrice.toString()
        ui.productType.text = product.Type
        ui.TasteNose.text = product.TasteNose
        ui.TastePalate.text = product.TastePalate
        ui.TasteFinish.text = product.TasteFinish
        ui.productVolume.text = product.Volume.toString()

        //Retrieve the image from DB
        val storageRef = FirebaseStorage.getInstance().reference.child("Images/${product.Image}")
        val localfile = File.createTempFile("tempImage", "jpg")
        storageRef.getFile(localfile).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeFile(localfile.absolutePath)
            ui.targetImage.setImageBitmap(bitmap)
        }.addOnFailureListener {
            //Toast.makeText(this, "Failed to retrieve the image", Toast.LENGTH_SHORT).show()
        }

    }



}