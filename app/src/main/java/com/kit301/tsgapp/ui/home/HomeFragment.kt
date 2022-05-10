package com.kit301.tsgapp.ui.home

import android.content.Intent
import android.graphics.BitmapFactory
import android.icu.lang.UCharacter
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.kit301.tsgapp.*
import com.kit301.tsgapp.FIREBASE_TAG
import com.kit301.tsgapp.databinding.FragmentHomeBinding
import com.kit301.tsgapp.databinding.ProductListItemBinding
import java.io.File

const val FIREBASE_TAG = "FirebaseLogging"   //Print things to the console for debugging database error

const val ProductIndex = "Product_Index"

val items = mutableListOf<Product>()

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        //val textView: TextView = binding.textHome
        //homeViewModel.text.observe(viewLifecycleOwner) {
        //    textView.text = it
        //}

        binding.floatCamera.setOnClickListener {
            val i = Intent(context, TakePhoto::class.java)
            startActivity(i)
        }

        //link the RecyclerView to your adapter
        binding.productList.adapter = ProductAdapter(tasProduct = items)

        //Tell your RecyclerView how to display the items
        binding.productList.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)

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
                    (binding.productList.adapter as ProductAdapter).notifyDataSetChanged()
                }


        //Retrieve Image from Database
        val ImageButton1 : ImageButton = binding.promotionImage
        val storageRef = FirebaseStorage.getInstance().reference.child("Images/006.jpg")
        val localfile = File.createTempFile("tempImage", "jpg")
        storageRef.getFile(localfile).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeFile(localfile.absolutePath)
            ImageButton1.setImageBitmap(bitmap)

        }.addOnFailureListener{
            //Toast.makeText(this, "Failed to retrieve the image", Toast.LENGTH_SHORT).show()

        }

        //Set the action when the ImageButton is clicked
        ImageButton1.setOnClickListener{


        }



        return root

    }

    inner class ProductHolder(var ui: ProductListItemBinding) : RecyclerView.ViewHolder(ui.root) {}

    //The adapter is the controller that handles the communication between our model and our view.
    inner class ProductAdapter(private val tasProduct: MutableList<Product>) : RecyclerView.Adapter<ProductHolder>() {

        //inflates a new row, and wraps it in a new ViewHolder
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeFragment.ProductHolder {
            val ui = ProductListItemBinding.inflate(layoutInflater, parent, false)   //inflate a new row from the my_list_item.xml
            return ProductHolder(ui)                                             //wrap it in a ViewHolder
        }

        override fun onBindViewHolder(holder: ProductHolder, position: Int) {
            val product = tasProduct[position]   //get the data at the requested position
            holder.ui.txtName.text = product.Name //set the TextView in the row we are recycling

            holder.itemView.setOnClickListener {
                val i = Intent(context, ProductDetails::class.java)
                i.putExtra(ProductIndex, position)
                startActivity(i)

                    }
        }

        override fun getItemCount(): Int {
            return tasProduct.size
        }
    }


}