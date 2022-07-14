package com.kit301.tsgapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kit301.tsgapp.databinding.ActivityDataNotFoundBinding
import com.kit301.tsgapp.ui.homepage.Homepage

class DataNotFound : DrawerBaseActivity() {

    private lateinit var ui : ActivityDataNotFoundBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ui = ActivityDataNotFoundBinding.inflate(layoutInflater)
        setContentView(ui.root)
        allocateActivityTitle("Result")

        ui.btnScanOtherProduct.setOnClickListener{
            val intent = Intent(this, Homepage::class.java)
            startActivity(intent)

        }


    }
}