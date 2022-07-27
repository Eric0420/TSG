package com.kit301.tsgapp.ui.test

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.firebase.auth.FirebaseAuth
import com.kit301.tsgapp.*
import com.kit301.tsgapp.databinding.ActivityProductDetailsBinding
import com.kit301.tsgapp.databinding.ActivityTestBinding
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern


const val FIREBASE_TAG = "FirebaseLogging"   //Print things to the console for debugging database error



class Test : DrawerBaseActivity() {
    private lateinit var ui : ActivityTestBinding


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ui = ActivityTestBinding.inflate(layoutInflater)
        setContentView(ui.root)
        setActionbarTitle()



        //getTime()
        //Test adding data
        //val db = Firebase.firestore
//
        //val AddProduct = Product(
        //        Name = "Classic Cask Strength (100ml)",
        //        SingleCaskNumber = null,
        //        AgeStatement = null,
        //        ABV = "58.00%",
        //        YearOfRelease = null,
        //        Volume = 100,
        //        AverageSalesPrice = 69.99,
        //        Type = "单一麦芽威士忌",
        //        Image = "019.jpg",
        //        TasteNose = "肉桂、肉豆蔻、甘草和橙皮，带有香草、奶油糖果和橡木的香气",
        //        TastePalate = "浓郁的香草和圣诞蛋糕的甜味, 柑橘和奶油糖果口味, 带有炖水果、枫糖浆和淡淡的塔斯马尼亚泥炭烟味伴随着复杂的柑橘和橡木味道",
        //        TasteFinish = "持久的柑橘、香料和太妃糖的香气",
        //        BarcodeNumber = ""
        //)
//
        //var ProductCollection = db.collection("zhProduct")
        //ProductCollection.document(AddProduct.Name!!)
        //        .set(AddProduct)
        //        .addOnSuccessListener {
        //            Log.d(FIREBASE_TAG, "Document created with product name")
        //        }
        //        .addOnFailureListener {
        //            Log.e(FIREBASE_TAG, "Error writing document")
        //        }




    }



    //private fun getTime(){
    //    val datetimeFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
    //    val date = Date()
    //    ui.textTime.text = datetimeFormat.format(date)
    //}


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



}