package com.kit301.tsgapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kit301.tsgapp.R

class PromotionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_promotion)
        supportActionBar?.title = "Promotions";
    }
}