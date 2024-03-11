package com.example.eindopdracht;

import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.eindopdracht.ApiCall;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.PreferenceManager;

import com.example.eindopdracht.Stock;

public class StockDetail extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        setAppTheme();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stock_detail);

        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Stock stock = (Stock) getIntent().getSerializableExtra("stockObject");

        StockDetailFragment stockDetailFragment = new StockDetailFragment();

        Bundle bundle = new Bundle();
        bundle.putSerializable("stockObject", stock);
        stockDetailFragment.setArguments(bundle);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, stockDetailFragment);
        fragmentTransaction.commit();

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
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
