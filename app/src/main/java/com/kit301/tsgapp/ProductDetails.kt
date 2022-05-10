package com.kit301.tsgapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kit301.tsgapp.databinding.ActivityProductDetailsBinding

class ProductDetails : AppCompatActivity() {

    private lateinit var ui : ActivityProductDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ui = ActivityProductDetailsBinding.inflate(layoutInflater)
        setContentView(ui.root)


    }
}