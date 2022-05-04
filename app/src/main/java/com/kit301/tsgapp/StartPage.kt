package com.kit301.tsgapp

import android.content.Intent
import android.os.Bundle
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity


class StartPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Select language spinner/dropdown
        val spinner: Spinner = findViewById(R.id.start_spinner)
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
                this,
                R.array.language_array,
                android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }


        // start button
        val button: Button = findViewById(R.id.toHome_btn)
        button.setOnClickListener {
            val language: String = spinner.selectedItem.toString()
            val myIntent = Intent(this, HomePage::class.java)
            myIntent.putExtra(EXTRA_MESSAGE, language) //Optional parameters
            startActivity(myIntent)
        }

    }

}