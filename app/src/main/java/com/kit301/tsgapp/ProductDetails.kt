package com.kit301.tsgapp

import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.kit301.tsgapp.databinding.ActivityProductDetailsBinding
import com.kit301.tsgapp.ui.home.ProductIndex
import com.kit301.tsgapp.ui.home.items
import java.io.File

class ProductDetails : AppCompatActivity() {

    private lateinit var ui : ActivityProductDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ui = ActivityProductDetailsBinding.inflate(layoutInflater)
        setContentView(ui.root)

        //Retrieve the product extra information from previous page
        val productID =intent.getIntExtra(ProductIndex, -1)
        var productObject = items[productID]

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