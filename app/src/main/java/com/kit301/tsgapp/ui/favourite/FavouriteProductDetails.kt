package com.kit301.tsgapp.ui.favourite

import android.app.Activity
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.kit301.tsgapp.DrawerBaseActivity
import com.kit301.tsgapp.FIREBASE_TAG
import com.kit301.tsgapp.Product
import com.kit301.tsgapp.databinding.ActivityProductDetailsBinding
import java.io.File
import java.util.*

//Connect to DB and retrieve the product details
val db = Firebase.firestore

//This variable use to control the action of the Favourite button
var isMyFavourite : Boolean = false

@Suppress("DEPRECATION")
class FavouriteProductDetails : DrawerBaseActivity() {

    private lateinit var ui : ActivityProductDetailsBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ui = ActivityProductDetailsBinding.inflate(layoutInflater)
        setContentView(ui.root)
        setActionbarTitle()


        //Retrieve the product extra information from previous page
        val favoutiteProductID =intent.getIntExtra(FavouriteProductIndex, -1)
        val favouriteProductObject = FavouriteItems[favoutiteProductID]
        val barcodeNumber = favouriteProductObject.BarcodeNumber

        //Setup the user interface
        setupUI(favouriteProductObject)

        //Check the product favourite status
        checkFavouriteStatus(favouriteProductObject)



        ui.btnFavourite.setOnClickListener {

            //If the current product exist in favourite list
            if (isMyFavourite) {
                ui.btnFavourite.backgroundTintList = ColorStateList.valueOf(Color.BLACK)
                isMyFavourite = false

                //Delete the product from favourite database
                deleteFavouriteProduct(barcodeNumber)
            }

            //If the current product not exist in favourite list
            else {
                ui.btnFavourite.backgroundTintList = ColorStateList.valueOf(Color.RED)
                isMyFavourite = true

                //Add the product to favourite database
                addFavouriteProduct(barcodeNumber)

            }

        }

        //Share product image
        ui.btnShare.setOnClickListener {
            this.window.decorView.isDrawingCacheEnabled = true
            val bmp: Bitmap = this.window.decorView.drawingCache
            val uri= Uri.parse(MediaStore.Images.Media.insertImage(contentResolver, bmp, "IMG"+ Calendar.getInstance().time, null))
            val intent=Intent(Intent.ACTION_SEND)
            intent.type = "image/jpeg"
            intent.putExtra(Intent.EXTRA_STREAM, uri)
            startActivity(Intent.createChooser(intent, "Share"))
        }


    }

    private fun setActionbarTitle() {
        val sharedPreferences = getSharedPreferences("Settings", Activity.MODE_PRIVATE)
        val currentLanguageSetting = sharedPreferences.getString("My_Lang", "")
        if (currentLanguageSetting == "en" || currentLanguageSetting == ""){
            allocateActivityTitle("Favourite Product Details")
        }
        else if (currentLanguageSetting == "zh"){
            allocateActivityTitle("收藏商品详细信息")
        }
    }


    private fun addFavouriteProduct(barcodeNumber: String?) {

        val enProductsCollection = db.collection("enProduct")  //The English Language product database
        val zhProductsCollection = db.collection("zhProduct")  ////The Chinese Language product database
        val deviceID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID)

        //Add the English Version Data to the UserFavouriteProduct Database
        enProductsCollection
                .whereEqualTo("barcodeNumber","$barcodeNumber")  //This is a Query for searching the document which barcodeNum is: "${barcodeNum}"
                .get()
                .addOnSuccessListener { result->
                    for (document in result) {
                        val favouriteProduct = document.toObject<Product>()

                        val favouriteProductCollection = db.collection("UserFavouriteProduct")
                        favouriteProductCollection.document(deviceID)
                                .collection("en")
                                .document("${favouriteProduct.Name}")
                                .set(favouriteProduct)
                                .addOnSuccessListener {
                                    //Display the text to tell the user that the action is success
                                    Toast.makeText(this, "Successfully added to you favourite list", Toast.LENGTH_SHORT).show()
                                    Log.d(FIREBASE_TAG, "Document created with product name: ${favouriteProduct.Name}")
                                }
                                .addOnFailureListener {
                                    Toast.makeText(this, "Failed to add to favourite list", Toast.LENGTH_SHORT).show()
                                    Log.e(FIREBASE_TAG, "Error writing document")
                                }

                    }
                }

        //Add the Chinese Version Data to the UserFavouriteProduct Database
        zhProductsCollection
                .whereEqualTo("barcodeNumber","$barcodeNumber")  //This is a Query for searching the document which barcodeNum is: "${barcodeNum}"
                .get()
                .addOnSuccessListener { result->
                    for (document in result) {
                        val favouriteProduct = document.toObject<Product>()

                        val favouriteProductCollection = db.collection("UserFavouriteProduct")
                        favouriteProductCollection.document(deviceID)
                                .collection("zh")
                                .document("${favouriteProduct.Name}")
                                .set(favouriteProduct)
                                .addOnSuccessListener {
                                    //Display the text to tell the user that the action is success
                                    Toast.makeText(this, "Successfully added to you favourite list", Toast.LENGTH_SHORT).show()
                                    Log.d(FIREBASE_TAG, "Document created with product name: ${favouriteProduct.Name}")
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
                .whereEqualTo("barcodeNumber","$barcodeNumber")  //This is a Query for searching the document which barcodeNum is: "${barcodeNum}"
                .get()
                .addOnSuccessListener { result->
                    for (document in result) {
                        val favouriteProduct = document.toObject<Product>()

                        val favouriteProductCollection = db.collection("UserFavouriteProduct")
                        favouriteProductCollection.document(deviceID)
                                .collection("en")
                                .document("${favouriteProduct.Name}")
                                .delete()
                                .addOnSuccessListener {
                                    Toast.makeText(this, "Successfully remove the product from your favourite list", Toast.LENGTH_SHORT).show()
                                    Log.d(FIREBASE_TAG, "Successfully deleted with product name: ${favouriteProduct.Name}")
                                }
                                .addOnFailureListener {
                                    Log.e(FIREBASE_TAG, "Error in deleting document")
                                }


                    }
                }

        //Delete the Chinese Version Data from the UserFavouriteProduct Database
        zhProductsCollection
                .whereEqualTo("barcodeNumber","$barcodeNumber")  //This is a Query for searching the document which barcodeNum is: "${barcodeNum}"
                .get()
                .addOnSuccessListener { result->
                    for (document in result) {
                        val favouriteProduct = document.toObject<Product>()

                        val favouriteProductCollection = db.collection("UserFavouriteProduct")
                        favouriteProductCollection.document(deviceID)
                                .collection("zh")
                                .document("${favouriteProduct.Name}")
                                .delete()
                                .addOnSuccessListener {
                                    Toast.makeText(this, "Successfully remove the product from your favourite list", Toast.LENGTH_SHORT).show()
                                    Log.d(FIREBASE_TAG, "Successfully deleted with product name: ${favouriteProduct.Name}")
                                }
                                .addOnFailureListener {
                                    Log.e(FIREBASE_TAG, "Error in deleting document")
                                }


                    }
                }

    }

    private fun checkFavouriteStatus(favouriteProductObject: Product) {

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
                .document("${favouriteProductObject.Name}")
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

    private fun setupUI(favouriteProductObject: Product) {
        //Setup the user interface
        ui.productName.text =favouriteProductObject.Name
        ui.productABV.text = favouriteProductObject.ABV
        ui.productYear.text = favouriteProductObject.YearOfRelease.toString()
        ui.productPrice.text = favouriteProductObject.AverageSalesPrice.toString()
        ui.productType.text = favouriteProductObject.Type
        ui.TasteNose.text = favouriteProductObject.TasteNose
        ui.TastePalate.text = favouriteProductObject.TastePalate
        ui.TasteFinish.text = favouriteProductObject.TasteFinish
        ui.productVolume.text = favouriteProductObject.Volume.toString()

        //Retrieve the image from DB
        val storageRef = FirebaseStorage.getInstance().reference.child("Images/${favouriteProductObject.Image}")
        val localfile = File.createTempFile("tempImage", "jpg")
        storageRef.getFile(localfile).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeFile(localfile.absolutePath)
            ui.targetImage.setImageBitmap(bitmap)
        }.addOnFailureListener {
            //Toast.makeText(this, "Failed to retrieve the image", Toast.LENGTH_SHORT).show()

        }

    }
}