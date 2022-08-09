package com.kit301.tsgapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.WindowManager;
import android.widget.TextView;

import com.kit301.tsgapp.ui.homepage.Homepage;

import static android.content.ContentValues.TAG;

@SuppressWarnings("ALL")
public class StartAnimation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_animation);

        //Hide the navigation bar
        getSupportActionBar().hide();

        //Hide the status bar
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);



        //TextView textView = findViewById(R.id.textStartAnimation);
        //textView.animate().translationX(1000).setDuration(1000).setStartDelay(2500);


        Thread thread = new Thread() {
          public void run(){
              try {
                  Thread.sleep(2600);
              }

              catch (Exception e){
                  e.printStackTrace();
              }

              finally {
                  Intent intent = new Intent(StartAnimation.this, Homepage.class);
                  startActivity(intent);
                  finish();
              }
          }
        };

        thread.start();
    }



}