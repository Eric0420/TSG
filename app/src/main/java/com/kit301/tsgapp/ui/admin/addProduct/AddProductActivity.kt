package com.kit301.tsgapp.ui.admin.addProduct

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.kit301.tsgapp.DrawerBaseActivity
import com.kit301.tsgapp.Product
import com.kit301.tsgapp.databinding.ActivityAddProductBinding
import com.kit301.tsgapp.isMyFavourite
import com.kit301.tsgapp.ui.search.ProductItems

class AddProductActivity : DrawerBaseActivity() {

    private lateinit var ui: ActivityAddProductBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ui = ActivityAddProductBinding.inflate(layoutInflater)
        setContentView(ui.root)
        setActionbarTitle()

        ui.btnAdd.setOnClickListener {
            if (ui.editName.text.isEmpty()){
                val builder = AlertDialog.Builder(this)
                builder.setMessage("Product Name cannot be empty.")
                    .setPositiveButton("OK",
                        DialogInterface.OnClickListener { _, _ ->
                        })
                builder.create()
                builder.show()
            }else{
                val newProduct = Product(
                    id = null,
                    Name = if(ui.editName.text.isNotEmpty()) ui.editName.text.toString() else null,
                    SingleCaskNumber = if(ui.editCask.text.isNotEmpty()) ui.editCask.text.toString() else null,
                    AgeStatement = null,
                    ABV = if(ui.editAdv.text.isNotEmpty()) ui.editAdv.text.toString() + '%' else null,
                    YearOfRelease = if(ui.editYear.text.isNotEmpty()) ui.editYear.text.toString().toInt() else null,
                    Volume = if(ui.editVolume.text.isNotEmpty()) ui.editVolume.text.toString().toInt() else null,
                    AverageSalesPrice = if(ui.editPrice.text.isNotEmpty()) ui.editPrice.text.toString().toDouble() else null,
                    Type = if(ui.editType.text.isNotEmpty()) ui.editType.text.toString() else null,
                    TasteNose = if(ui.editNose.text.isNotEmpty()) ui.editNose.text.toString() else null,
                    TastePalate = if(ui.editPalate.text.isNotEmpty()) ui.editPalate.text.toString() else null,
                    TasteFinish = if(ui.editFinish.text.isNotEmpty()) ui.editFinish.text.toString() else null,
                    Image = null,
                    BarcodeNumber = if(ui.editBarcode.text.isNotEmpty()) ui.editBarcode.text.toString() else null
                )

                val db = Firebase.firestore

                val languageFlag =
                    if (getSharedPreferences("Settings", Activity.MODE_PRIVATE).getString("My_Lang", "") == "en" || getSharedPreferences("Settings", Activity.MODE_PRIVATE).getString("My_Lang", "") == ""){
                        "enProduct"
                    }else{
                        "zhProduct"
                    }


                //Check whether or not the current product is in database
                val checkExistingProduct = FirebaseFirestore.getInstance().collection("$languageFlag")
                        .document("${newProduct.Name.toString()}")
                checkExistingProduct.get().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val document = task.result
                        if (document != null) {
                            if (document.exists()) {

                                val builder = AlertDialog.Builder(this)
                                builder.setMessage("This product already exist in database")
                                        .setPositiveButton("OK",
                                                DialogInterface.OnClickListener { _, _ ->
                                                    finish()
                                                })
                                builder.create()
                                builder.show()
                            } else {
                                //Retrieve data from database
                                val productCollection = db.collection(languageFlag)
                                productCollection.document(newProduct.Name!!)
                                        .set(newProduct)
                                        .addOnSuccessListener {
                                            ProductItems.add(newProduct)
                                            val builder = AlertDialog.Builder(this)
                                            builder.setMessage("Successfully add ${newProduct.Name} to database.")
                                                    .setPositiveButton("OK",
                                                            DialogInterface.OnClickListener { _, _ ->
                                                                finish()
                                                            })
                                            builder.create()
                                            builder.show()
                                        }
                            }
                        }
                    } else {
                        Log.d("TAG", "Error: ", task.exception)
                    }
                }


            }

        }

    }

    private fun setActionbarTitle() {
        val sharedPreferences = getSharedPreferences("Settings", Activity.MODE_PRIVATE)
        val currentLanguageSetting = sharedPreferences.getString("My_Lang", "")
        if (currentLanguageSetting == "en" || currentLanguageSetting == ""){
            allocateActivityTitle("Add Product")
        }
        else if (currentLanguageSetting == "zh"){
            allocateActivityTitle("添加产品")
        }
    }
}