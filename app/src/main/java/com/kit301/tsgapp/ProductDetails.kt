package com.kit301.tsgapp

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.documentfile.provider.DocumentFile
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.kit301.tsgapp.databinding.ActivityProductDetailsBinding
import com.kit301.tsgapp.ui.home.ProductIndex
import com.kit301.tsgapp.ui.home.items

import java.io.File

class ProductDetails : AppCompatActivity() {

    //This variable use to control the action of the Favourite button
    var isMyFavourite : Boolean = false


    private lateinit var ui : ActivityProductDetailsBinding

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ui = ActivityProductDetailsBinding.inflate(layoutInflater)
        setContentView(ui.root)

        //Retrieve the product extra information from previous page
        val productID =intent.getIntExtra(ProductIndex, -1)
        var productObject = items[productID]

        //Get the Android Device ID for identifying different devices
        var deviceID = Settings.Secure.getString(getContentResolver(),Settings.Secure.ANDROID_ID)


        //Connect to DB and retrieve the product details
        val db = Firebase.firestore
        var productsCollection = db.collection("Product")

        ui.productName.text =productObject.Name
        ui.productABV.text = productObject.ABV
        ui.productYear.text = productObject.YearOfRelease.toString()
        ui.productPrice.text = productObject.AverageSalesPrice.toString()
        ui.productType.text = productObject.Type
        ui.TasteNose.text = productObject.TasteNose
        ui.TastePalate.text = productObject.TastePalate
        ui.TasteFinish.text = productObject.TasteFinish

        //Check whether or not the current product is in favourite list
        val checkFavouriteStatus = FirebaseFirestore.getInstance().collection("UserFavouriteProduct").document(deviceID).collection("UserFavourite").document("${productObject.Name}")
        checkFavouriteStatus.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val document = task.result
                if(document != null) {
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



        ui.btnBack.setOnClickListener{
            val intent = Intent(this,HomePage::class.java)
            startActivity(intent)

        }


        ui.btnFavourite.setOnClickListener{

            //Retrieve the product information, ready for next action
                val AddFavouriteProduct = Product(
                        Name = productObject.Name,
                        SingleCaskNumber = productObject.SingleCaskNumber,
                        AgeStatement = productObject.AgeStatement,
                        ABV = productObject.ABV,
                        YearOfRelease = productObject.YearOfRelease,
                        Volume = productObject.Volume,
                        AverageSalesPrice = productObject.AverageSalesPrice,
                        Type = productObject.Type,
                        Image = productObject.Image,
                        TasteNose = productObject.TasteNose,
                        TastePalate = productObject.TastePalate,
                        TasteFinish = productObject.TasteFinish
                )

            //If the current product exist in favourite list
            if(isMyFavourite){
                ui.btnFavourite.backgroundTintList = ColorStateList.valueOf(Color.BLACK)

                var ProductCollection = db.collection("UserFavouriteProduct")
                        ProductCollection.document(deviceID)
                                .collection("UserFavourite")
                                .document("${AddFavouriteProduct.Name}")
                                .delete()
                                .addOnSuccessListener {
                                    Toast.makeText(this,"Successfully remove the product from your favourite list",Toast.LENGTH_SHORT).show()
                                    Log.d(FIREBASE_TAG, "Successfully deleted with product name: ${AddFavouriteProduct.Name}")
                                }
                                .addOnFailureListener {
                                    Log.e(FIREBASE_TAG, "Error in deleting document")
                                }

            }

            //If the current product not exist in favourite list
            else{
                ui.btnFavourite.backgroundTintList = ColorStateList.valueOf(Color.RED)

                var ProductCollection = db.collection("UserFavouriteProduct")
                        ProductCollection.document(deviceID)
                                .collection("UserFavourite")
                                .document("${AddFavouriteProduct.Name}")
                                .set(AddFavouriteProduct)

                                .addOnSuccessListener {
                                    //Display the text to tell the user that the action is success
                                    Toast.makeText(this,"Successfully added to you favourite list",Toast.LENGTH_SHORT).show()
                                    Log.d(FIREBASE_TAG, "Document created with product name: ${AddFavouriteProduct.Name}")
                                }
                                .addOnFailureListener {
                                    Toast.makeText(this,"Failed to add to favourite list",Toast.LENGTH_SHORT).show()
                                    Log.e(FIREBASE_TAG, "Error writing document")
                                }
            }


        }


        //Retrieve the image from DB
        val storageRef = FirebaseStorage.getInstance().reference.child("Images/${productObject.Image}")
        val localfile = File.createTempFile("tempImage", "jpg")
        storageRef.getFile(localfile).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeFile(localfile.absolutePath)
            ui.targetImage.setImageBitmap(bitmap)

        }.addOnFailureListener{
            //Toast.makeText(this, "Failed to retrieve the image", Toast.LENGTH_SHORT).show()

        }


    }


}