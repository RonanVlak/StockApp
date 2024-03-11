package com.example.eindopdracht;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setAppTheme();
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_container);

        MainFragment mainFragment = new MainFragment();
        StockDetailFragment detailFragment = new StockDetailFragment();
        // Check the device's orientation
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // Load landscape layout with both fragments
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_fragment_container, mainFragment)
                    .replace(R.id.stock_detail_fragment_container, detailFragment)
                    .commit();
        } else {


            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, mainFragment);
            fragmentTransaction.commit();
        }
        // Initialize and assign variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Set Home selected
        bottomNavigationView.setSelectedItemId(R.id.nav_settings);

        // Perform item selected listener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        return true;
                    case R.id.nav_settings:
                        startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });

    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            // Check if the StockDetailFragment is currently added and remove it
            StockDetailFragment detailFragment = (StockDetailFragment) fragmentManager.findFragmentById(R.id.stock_detail_fragment_container);
            if (detailFragment != null) {
                transaction.remove(detailFragment);
            }

            // Check if the MainFragment is currently added and remove it
            MainFragment mainFragment = (MainFragment) fragmentManager.findFragmentById(R.id.main_fragment_container);
            if (mainFragment != null) {
                transaction.remove(mainFragment);
            }

            // Add MainFragment if it's not already added
            mainFragment = (MainFragment) fragmentManager.findFragmentById(R.id.fragment_container);
            if (mainFragment == null) {
                mainFragment = new MainFragment();
                transaction.replace(R.id.fragment_container, mainFragment);
            }
        } else if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // Remove existing fragments if they exist
            MainFragment mainFragment = (MainFragment) fragmentManager.findFragmentById(R.id.fragment_container);
            if (mainFragment != null) {
                transaction.remove(mainFragment);
            }

            StockDetailFragment detailFragment = (StockDetailFragment) fragmentManager.findFragmentById(R.id.stock_detail_fragment_container);
            if (detailFragment != null) {
                transaction.remove(detailFragment);
            }

            // Add MainFragment if it's not already added
            mainFragment = (MainFragment) fragmentManager.findFragmentById(R.id.main_fragment_container);
            if (mainFragment == null) {
                mainFragment = new MainFragment();
                transaction.replace(R.id.main_fragment_container, mainFragment);
            }

            // Add StockDetailFragment if it's not already added
            detailFragment = (StockDetailFragment) fragmentManager.findFragmentById(R.id.stock_detail_fragment_container);
            if (detailFragment == null) {
                detailFragment = new StockDetailFragment();
                transaction.replace(R.id.stock_detail_fragment_container, detailFragment);
            }
        }
        transaction.commit();
    }
    private void setAppTheme() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean darkModeEnabled = sharedPreferences.getBoolean("switch_theme_key", true);
        if (darkModeEnabled) {
            setTheme(R.style.AppThemeDark);
        } else {
            setTheme(R.style.AppThemeLight);
        }
    }
}