package com.kit301.tsgapp

import android.graphics.BitmapFactory
import android.media.Image
import android.os.Bundle
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.util.Log
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.kit301.tsgapp.databinding.ProductListItemBinding
import java.io.File

const val FIREBASE_TAG = "FirebaseLogging"   //Print things to the console for debugging database error


class HomePage : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_profile, R.id.navigation_notifications
                , R.id.navigation_more
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        val language = intent.getStringExtra(EXTRA_MESSAGE)



        //Test adding data
        //val AddProduct = Product(
        //        Name = "Chinotto Cask II",
        //        SingleCaskNumber = null,
        //        AgeStatement = null,
        //        ABV = "49%",
        //        YearOfRelease = 2022,
        //        Volume = 500,
        //        AverageSalesPrice = 299.99,
        //        Type = "Single Malt",
        //        TasteNose = "Orange citrus leads, while peat smoke lift the aroma and intertwine wwith hints of toasted coconut and floral lemon",
        //        TastePalate = "Slight bitter toffee and smiked citrus arrive with vivacious ginger confectionery and milk chocolate",
        //        TasteFinish = "A long sweet smoke, interlaced with a light oak and jaffa cake influence"
        //)
        //
        //var ProductCollection = db.collection("Product")
        //ProductCollection.document(AddProduct.Name)
        //        .set(AddProduct)
        //        .addOnSuccessListener {
        //            Log.d(FIREBASE_TAG, "Document created with product name")
        //        }
        //        .addOnFailureListener {
        //            Log.e(FIREBASE_TAG, "Error writing document")
        //        }


    }

}

