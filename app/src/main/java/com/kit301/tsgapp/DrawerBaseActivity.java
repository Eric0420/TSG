package com.kit301.tsgapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.google.android.material.navigation.NavigationView;
import com.kit301.tsgapp.ui.about.AboutActivity;
import com.kit301.tsgapp.ui.admin.AdminActivity;
import com.kit301.tsgapp.ui.favourite.FavouriteProduct;
import com.kit301.tsgapp.ui.history.HistoryActivity;
import com.kit301.tsgapp.ui.homepage.Homepage;
import com.kit301.tsgapp.ui.notification.NotificationActivity;
import com.kit301.tsgapp.ui.search.SearchProduct;
import com.kit301.tsgapp.ui.settings.SettingsActivity;
import com.kit301.tsgapp.ui.test.Test;

public class DrawerBaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;

    @Override
    public void setContentView(View view) {
        drawerLayout = (DrawerLayout) getLayoutInflater().inflate(R.layout.activity_drawer_base,null);
        FrameLayout container = drawerLayout.findViewById(R.id.activityContainer);
        container.addView(view);
        super.setContentView(drawerLayout);

        Toolbar toolbar = drawerLayout.findViewById(R.id.testToolBar);
        setSupportActionBar(toolbar);

        NavigationView navigationView = drawerLayout.findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.menu_drawer_open,R.string.menu_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawerLayout.closeDrawer(GravityCompat.START);

        switch (item.getItemId()){
            case R.id.navigation_search:
                startActivity(new Intent(this, SearchProduct.class));
                overridePendingTransition(0,0);
                break;

            case R.id.navigation_favourite:
                startActivity(new Intent(this, FavouriteProduct.class));
                overridePendingTransition(0,0);
                break;

            case R.id.navigation_home:
                startActivity(new Intent(this, Homepage.class));
                overridePendingTransition(0,0);
                break;

            case R.id.navigation_test:
                startActivity(new Intent(this, Test.class));
                overridePendingTransition(0,0);
                break;

            case R.id.navigation_about:
                startActivity(new Intent(this, AboutActivity.class));
                overridePendingTransition(0,0);
                break;

            case R.id.navigation_admin:
                startActivity(new Intent(this, AdminActivity.class));
                overridePendingTransition(0,0);
                break;

            case R.id.navigation_history:
                startActivity(new Intent(this, HistoryActivity.class));
                overridePendingTransition(0,0);
                break;

            case R.id.navigation_notification:
                startActivity(new Intent(this, NotificationActivity.class));
                overridePendingTransition(0,0);
                break;

            case R.id.navigation_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                overridePendingTransition(0,0);
                break;

        }

        return false;
    }

    protected void allocateActivityTitle(String titleString){
        if(getSupportActionBar()!=null){
            getSupportActionBar().setTitle(titleString);

        }

    }
}