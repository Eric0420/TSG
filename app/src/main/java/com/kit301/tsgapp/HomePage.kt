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

val items = mutableListOf<Product>()

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

        //var ProductCollection = db.collection("Product")
        //ProductCollection.document(AddProduct.Name)
        //        .set(AddProduct)
        //        .addOnSuccessListener {
        //            Log.d(FIREBASE_TAG, "Document created with product name")
        //        }
        //        .addOnFailureListener {
        //            Log.e(FIREBASE_TAG, "Error writing document")
        //        }




        //link the RecyclerView to your adapter
        val myList : RecyclerView = findViewById(R.id.productList)
        myList.adapter = ProductAdapter(tasProduct = items)

        //Tell your RecyclerView how to display the items
        //vertical list
        myList.layoutManager = LinearLayoutManager(this)


        //Get database connection and connect to database
        val db = Firebase.firestore


        //Retrieve data from database
        var productsCollection = db.collection("Product")
        productsCollection
                .get()
                .addOnSuccessListener { result ->
                    items.clear() //this line clears the list, and prevents a bug where items would be duplicated upon rotation of screen
                    Log.d(FIREBASE_TAG, "--- all Product ---")
                    for (document in result)
                    {
                        //Log.d(FIREBASE_TAG, document.toString())
                        val product = document.toObject<Product>()
                        product.id = document.id
                        Log.d(FIREBASE_TAG, product.toString())

                        items.add(product)
                    }
                    (myList.adapter as ProductAdapter).notifyDataSetChanged()
                }


        //Retrieve Image from Database
        val ImageButton1 : ImageButton = findViewById(R.id.promotionImage)
        val storageRef = FirebaseStorage.getInstance().reference.child("Images/006.jpg")
        val localfile = File.createTempFile("tempImage", "jpg")
        storageRef.getFile(localfile).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeFile(localfile.absolutePath)
            ImageButton1.setImageBitmap(bitmap)

        }.addOnFailureListener{
            Toast.makeText(this, "Failed to retrieve the image", Toast.LENGTH_SHORT).show()
        }

        //Set the action when the ImageButton is clicked
        ImageButton1.setOnClickListener{

        }





    }
    inner class ProductHolder(var ui: ProductListItemBinding) : RecyclerView.ViewHolder(ui.root) {}

    //The adapter is the controller that handles the communication between our model and our view.
    inner class ProductAdapter(private val tasProduct: MutableList<Product>) : RecyclerView.Adapter<ProductHolder>() {

        //inflates a new row, and wraps it in a new ViewHolder
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomePage.ProductHolder {
            val ui = ProductListItemBinding.inflate(layoutInflater, parent, false)   //inflate a new row from the my_list_item.xml
            return ProductHolder(ui)
        }

        override fun onBindViewHolder(holder: ProductHolder, position: Int) {
            val product = tasProduct[position]   //get the data at the requested position
            holder.ui.txtName.text = product.Name //set the TextView in the row we are recycling

            holder.itemView.setOnClickListener {
                //your code here in next step
            }
        }

        override fun getItemCount(): Int {
            return tasProduct.size
        }
    }


}

