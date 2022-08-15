package com.kit301.tsgapp.ui.search

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.kit301.tsgapp.DrawerBaseActivity
import com.kit301.tsgapp.Product
import com.kit301.tsgapp.databinding.ActivitySearchProductBinding
import com.kit301.tsgapp.databinding.MyFavouriteListBinding
import com.kit301.tsgapp.ui.test.FIREBASE_TAG
import java.io.File
import java.util.*

const val ListProductIndex = "ListProduct_Index"
const val FilterProductIndex = "FilterProduct_Index"
val ProductItems = mutableListOf<Product>()
var filtered = mutableListOf<Product>()
var filterOn = false

class SearchProduct : DrawerBaseActivity() {

    private lateinit var ui : ActivitySearchProductBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ui = ActivitySearchProductBinding.inflate(layoutInflater)
        setContentView(ui.root)
        setActionbarTitle()

        //Link the RecyclerView to your adapter
        ui.productList.adapter = ListProductAdapter(ListProduct = ProductItems)

        //Tell your RecyclerView how to display the items, vertical list
        //ui.productList.layoutManager = LinearLayoutManager(this)
        //or a grid layout
        ui.productList.layoutManager = GridLayoutManager(this,2)

        //Get database connection and connect to database
        val db = Firebase.firestore
        var languageFlag = "enProduct"
        filterOn = false

        if (getSharedPreferences("Settings", Activity.MODE_PRIVATE).getString("My_Lang", "") == "en" || getSharedPreferences("Settings", Activity.MODE_PRIVATE).getString("My_Lang", "") == ""){
            languageFlag = "enProduct"
        }else{
            languageFlag = "zhProduct"
        }

        //Retrieve data from database
        var productCollection = db.collection(languageFlag)
        productCollection
                .get()
                .addOnSuccessListener { result ->
                    ProductItems.clear() //this line clears the list, and prevents a bug where items would be duplicated upon rotation of screen
                    Log.d(FIREBASE_TAG, "--- all Product ---")
                    for (document in result)
                    {
                        //Log.d(FIREBASE_TAG, document.toString())
                        val product = document.toObject<Product>()
                        product.id = document.id
                        Log.d(FIREBASE_TAG, product.toString())

                        ProductItems.add(product)
                    }
                    (ui.productList.adapter as SearchProduct.ListProductAdapter).notifyDataSetChanged()
                }

        ui.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // on below line we are checking
                // if query exist or not.

                if (query != null) {
                    // if query exist within list we
                    // are filtering our list adapter.
                    filtered = ProductItems.filter { it.Name?.contains(query) ?: false }.toMutableList()
                    ui.productList.adapter = ListProductAdapter(ListProduct = filtered)
                    filterOn = true
                } else {
                    // if query is not present we are displaying
                    // a toast message as no  data found..
                    Toast.makeText(baseContext, "No product found..", Toast.LENGTH_LONG)
                            .show()
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // if query text is change in that case we
                // are filtering our adapter with
                // new text on below line.
                return false
            }

        })
    }


    inner class ListProductHolder(var ui: MyFavouriteListBinding) : RecyclerView.ViewHolder(ui.root) {}

    //The adapter is the controller that handles the communication between our model and our view.
    inner class ListProductAdapter(private val ListProduct: MutableList<Product>) : RecyclerView.Adapter<ListProductHolder>() {

        //inflates a new row, and wraps it in a new ViewHolder
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchProduct.ListProductHolder {
            val ui = MyFavouriteListBinding.inflate(layoutInflater, parent, false)   //inflate a new row from the my_favourite_list.xml
            return ListProductHolder(ui)                                             //wrap it in a ViewHolder
        }

        override fun onBindViewHolder(holder: ListProductHolder, position: Int) {
            val product = ListProduct[position]   //get the data at the requested position
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

                var intent = Intent(holder.itemView.context, ListProductDetails::class.java)

                if (filterOn){
                    intent.putExtra(FilterProductIndex, position)
                }else{
                    intent.putExtra(ListProductIndex, position)
                }
                startActivity(intent)

            }
        }

        override fun getItemCount(): Int {
            return ListProduct.size
        }
    }

    private fun setActionbarTitle() {
        val sharedPreferences = getSharedPreferences("Settings", Activity.MODE_PRIVATE)
        val currentLanguageSetting = sharedPreferences.getString("My_Lang", "")

        if (currentLanguageSetting == "en" || currentLanguageSetting == ""){
            allocateActivityTitle("Search Product")
        }
        else if (currentLanguageSetting == "zh"){
            allocateActivityTitle("搜索商品")
        }
    }



}