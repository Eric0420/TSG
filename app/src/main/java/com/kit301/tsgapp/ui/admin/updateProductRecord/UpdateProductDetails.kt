package com.kit301.tsgapp.ui.admin.updateProductRecord

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.firestore.ktx.toObject
import com.kit301.tsgapp.*
import com.kit301.tsgapp.databinding.ActivityUpdateProductDetailsBinding


class UpdateProductDetails : DrawerBaseActivity() {

    private lateinit var ui: ActivityUpdateProductDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ui = ActivityUpdateProductDetailsBinding.inflate(layoutInflater)
        setContentView(ui.root)
        setActionbarTitle()

        //Get the barcode number and database option from previous activity
        val barcodeNumber = intent.getStringExtra(Search_Result)
        val currentLanguageSelection = intent.getStringExtra(Current_Language_Selection)

        getProductRecord(barcodeNumber, currentLanguageSelection)



    }







    private fun getProductRecord(barcodeNumber: String?, currentLanguageSelection: String?) {

        val productsCollection = db.collection("$currentLanguageSelection" + "Product")

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
                        val product = document.toObject<Product>()

                        //This value is use to compare the admin input product name
                        //If admin update the product name, create the new product record replace the old one, and delete the old product record
                        val oldProductName = product.Name.toString()

                        setupUserInterface(product)


                        //The action of the Change Details button
                        ui.btnChangeDetails.setOnClickListener {
                            val newProductName = ui.txtProductName.text.toString()

                            if (oldProductName == newProductName){
                                updateProductDetails(product, currentLanguageSelection)
                            }
                            else{

                                deleteOldProductRecord(product, currentLanguageSelection)

                                updateProductDetails(product, currentLanguageSelection)
                            }



                        }

                    }
                }

            }

            .addOnFailureListener{
                Log.d(FIREBASE_TAG, "Error getting documents")
                Toast.makeText(this,"Failed", Toast.LENGTH_SHORT).show()
            }


    }

    private fun deleteOldProductRecord(product: Product, currentLanguageSelection: String?) {
        val productsCollection = db.collection("$currentLanguageSelection" + "Product")

        productsCollection.document("${product.Name}")
                .delete()
                .addOnSuccessListener {
                    Toast.makeText(this, "Successfully update the product information", Toast.LENGTH_SHORT).show()
                    Log.d(FIREBASE_TAG, "Successfully deleted with product name: ${product.Name}")
                }
                .addOnFailureListener {
                    Log.e(FIREBASE_TAG, "Error in deleting document")
                }

    }

    private fun updateProductDetails(product: Product, currentLanguageSelection: String?) {
        val productsCollection = db.collection("$currentLanguageSelection" + "Product")

        product.Name = ui.txtProductName.text.toString()

        if (ui.txtSingleCastNumber.text.isNullOrBlank()){
            product.SingleCaskNumber = null
        }
        else {
            product.SingleCaskNumber = ui.txtSingleCastNumber.text.toString()
        }

        if (ui.txtAgeStatement.text.isNullOrBlank()){
            product.AgeStatement = null
        }
        else{
            product.AgeStatement = ui.txtAgeStatement.text.toString().toInt()
        }

        if (ui.txtABV.text.isNullOrBlank()){
            product.ABV = null
        }
        else {
            product.ABV = ui.txtABV.text.toString()
        }

        if (ui.txtYearOfRelease.text.isNullOrBlank()){
            product.YearOfRelease = null
        }
        else {
            product.YearOfRelease = ui.txtYearOfRelease.text.toString().toInt()
        }

        product.Volume = ui.txtVolume.text.toString().toInt()
        product.AverageSalesPrice = ui.txtAverageSalesPrice.text.toString().toDouble()
        product.Type = ui.txtProductType.text.toString()
        product.TasteNose = ui.txtTasteNose.text.toString()
        product.TastePalate = ui.txtTastePalate.text.toString()
        product.TasteFinish = ui.txtTasteFinish.text.toString()


        productsCollection.document(product.Name!!)
                .set(product)
                .addOnSuccessListener {
                    Log.d(FIREBASE_TAG, "Successfully updated product details")
                    //return to the list
                    finish()
                }


    }


    private fun setupUserInterface(product: Product) {
        ui.txtBarcodeNumber.text = product.BarcodeNumber
        ui.txtProductName.setText(product.Name)

        if (product.SingleCaskNumber == null || product.SingleCaskNumber == ""){
            //ui.txtSingleCastNumber.setText("Null")
            ui.txtSingleCastNumber.hint = "Null"
        }
        else {
            ui.txtSingleCastNumber.setText(product.SingleCaskNumber)
        }

        if(product.AgeStatement == null){
            //ui.txtAgeStatement.setText("Null")
            ui.txtAgeStatement.hint = "Null"
        }
        else {
            ui.txtAgeStatement.setText(product.AgeStatement.toString())
        }

        if (product.ABV == null){
            ui.txtABV.hint = "Null"
        }
        else {
            ui.txtABV.setText(product.ABV)
        }

        if (product.YearOfRelease == null){
            //ui.txtYearOfRelease.setText("Null")
            ui.txtYearOfRelease.hint = "Null"
        }
        else {
            ui.txtYearOfRelease.setText(product.YearOfRelease.toString())
        }

        ui.txtVolume.setText(product.Volume.toString())
        ui.txtAverageSalesPrice.setText(product.AverageSalesPrice.toString())
        ui.txtProductType.setText(product.Type)
        ui.txtTasteNose.setText(product.TasteNose)
        ui.txtTastePalate.setText(product.TastePalate)
        ui.txtTasteFinish.setText(product.TasteFinish)


    }

    private fun setActionbarTitle() {
        val sharedPreferences = getSharedPreferences("Settings", Activity.MODE_PRIVATE)
        val currentLanguageSetting = sharedPreferences.getString("My_Lang", "")
        if (currentLanguageSetting == "en" || currentLanguageSetting == ""){
            allocateActivityTitle("Update Product Information")
        }
        else if (currentLanguageSetting == "zh"){
            allocateActivityTitle("更新产品信息")
        }
    }


}