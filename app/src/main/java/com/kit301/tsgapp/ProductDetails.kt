package com.kit301.tsgapp

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.kit301.tsgapp.databinding.ActivityProductDetailsBinding
import com.kit301.tsgapp.ui.home.ProductIndex
import com.kit301.tsgapp.ui.home.items

import java.io.File

class ProductDetails : AppCompatActivity() {

    //This variable use to control the action of the Favourite button
    var buttonClick = 0

    private lateinit var ui : ActivityProductDetailsBinding

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ui = ActivityProductDetailsBinding.inflate(layoutInflater)
        setContentView(ui.root)

        //Retrieve the product extra information from previous page
        val productID =intent.getIntExtra(ProductIndex, -1)
        var productObject = items[productID]

        when(intent.getIntExtra(ProductIndex, -1)){


        }


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




        ui.btnBack.setOnClickListener{
            val intent = Intent(this,HomePage::class.java)
            startActivity(intent)

        }

        ui.btnFavourite.setOnClickListener{

            buttonClick+=1


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

            //Add the product to DB when user client the Favourite button
            if(buttonClick %2 == 1) {
                ui.btnFavourite.backgroundTintList = ColorStateList.valueOf(Color.RED)

                var ProductCollection = db.collection("Favourite")
                ProductCollection.document(AddFavouriteProduct.Name.toString())
                        .set(AddFavouriteProduct)
                        .addOnSuccessListener {
                            Log.d(FIREBASE_TAG, "Document created with product name: ${AddFavouriteProduct.Name}")
                        }
                        .addOnFailureListener {
                            Log.e(FIREBASE_TAG, "Error writing document")
                        }

            }


            //Delete the product to DB when user client the Favourite button
            if(buttonClick %2 == 0){
                ui.btnFavourite.backgroundTintList = ColorStateList.valueOf(Color.BLACK)

                var ProductCollection = db.collection("Favourite")
                ProductCollection.document(AddFavouriteProduct.Name.toString())
                        .delete()
                        .addOnSuccessListener {
                            Log.d(FIREBASE_TAG, "Successfully deleted with product name: ${AddFavouriteProduct.Name}")
                        }
                        .addOnFailureListener {
                            Log.e(FIREBASE_TAG, "Error in deleting document")
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