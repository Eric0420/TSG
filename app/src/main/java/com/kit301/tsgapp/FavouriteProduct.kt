package com.kit301.tsgapp

import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.kit301.tsgapp.databinding.ActivityFavouriteProductBinding
import com.kit301.tsgapp.databinding.ActivityProductDetailsBinding
import com.kit301.tsgapp.databinding.MyFavouriteListBinding
import com.kit301.tsgapp.ui.home.HomeFragment
import com.kit301.tsgapp.ui.home.items
import com.kit301.tsgapp.ui.more.MoreFragment
import java.io.File

const val FavouriteProductIndex = "FavouriteProduct_Index"

val FavouriteItems = mutableListOf<Product>()

class FavouriteProduct : AppCompatActivity() {

    private lateinit var ui : ActivityFavouriteProductBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ui = ActivityFavouriteProductBinding.inflate(layoutInflater)
        setContentView(ui.root)


        //Link the RecyclerView to your adapter
        ui.favouriteList.adapter = FavouriteProductAdapter(FavouriteProduct = FavouriteItems)

        //Tell your RecyclerView how to display the items, vertical list
        //ui.favouriteList.layoutManager = LinearLayoutManager(this)
        //or a grid layout
        ui.favouriteList.layoutManager = GridLayoutManager(this,2)


        //Get database connection and connect to database
        val db = Firebase.firestore

        //Get the Android Device ID for identifying different devices
        var deviceID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID)

        //Retrieve data from database
        var favouriteCollection = db.collection("UserFavouriteProduct")
        favouriteCollection.document(deviceID)
                .collection("UserFavourite")
                .get()
                .addOnSuccessListener { result ->
                    FavouriteItems.clear() //this line clears the list, and prevents a bug where items would be duplicated upon rotation of screen
                    Log.d(FIREBASE_TAG, "--- all Favourite Product ---")
                    for (document in result)
                    {
                        //Log.d(FIREBASE_TAG, document.toString())
                        val product = document.toObject<Product>()
                        product.id = document.id
                        Log.d(FIREBASE_TAG, product.toString())

                        FavouriteItems.add(product)
                    }
                    (ui.favouriteList.adapter as FavouriteProductAdapter).notifyDataSetChanged()
                }

        ui.btnFavouriteBack.setOnClickListener{
            val intent = Intent(this, HomePage::class.java)
            startActivity(intent)

        }




    }

    inner class FavouriteProductHolder(var ui: MyFavouriteListBinding) : RecyclerView.ViewHolder(ui.root) {}

    //The adapter is the controller that handles the communication between our model and our view.
    inner class FavouriteProductAdapter(private val FavouriteProduct: MutableList<Product>) : RecyclerView.Adapter<FavouriteProductHolder>() {

        //inflates a new row, and wraps it in a new ViewHolder
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteProduct.FavouriteProductHolder {
            val ui = MyFavouriteListBinding.inflate(layoutInflater, parent, false)   //inflate a new row from the my_favourite_list.xml
            return FavouriteProductHolder(ui)                                             //wrap it in a ViewHolder
        }

        override fun onBindViewHolder(holder: FavouriteProductHolder, position: Int) {
            val product = FavouriteProduct[position]   //get the data at the requested position
            holder.ui.txtFavouriteProductName.text = product.Name

            val storageImage = FirebaseStorage.getInstance().reference.child("Images/${product.Image}")
            val localImage = File.createTempFile("tempImage", "jpg")
            storageImage.getFile(localImage).addOnSuccessListener {
                val bitmap = BitmapFactory.decodeFile(localImage.absolutePath)
                holder.ui.FavouriteProductImage.setImageBitmap(bitmap)

            }.addOnFailureListener{
                //Toast.makeText(this, "Failed to retrieve the image", Toast.LENGTH_SHORT).show()

            }



            holder.itemView.setOnClickListener {
                //var intent = Intent(holder.itemView.context, ProductDetails::class.java)
                //intent.putExtra(FavouriteProductIndex, position)
                //startActivity(intent)
            }
        }

        override fun getItemCount(): Int {
            return FavouriteProduct.size
        }
    }

}