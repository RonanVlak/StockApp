package com.example.eindopdracht;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.eindopdracht.ApiCall;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.eindopdracht.Stock;

public class StockDetail extends AppCompatActivity {
    private ListView listView;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stock_detail);
        Stock stock = (Stock) getIntent().getSerializableExtra("stockObject");

        String symbol = stock.getStockName();
        String price = stock.getPrice();
        String prevClose = stock.getPrevClose();
        String currency = stock.getCurrency();
        String exchangeName = stock.getExchangeName();
        String instrumentType = stock.getInstrumentType();
        String daychange = stock.getDaychange();


        String[] stockDetails = new String[] {
                symbol,
                price,
                "Previous Close: $" + prevClose,
                daychange,
                "Currency: " + currency,
                "Exchange Name: " + exchangeName,
                "Instrument Type: " + instrumentType
        };

        listView = findViewById(R.id.listView);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, stockDetails) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                if (position == 0) {
                    // Customize the first item as a title in bold
                    ((TextView) view).setTypeface(null, Typeface.BOLD);
                    ((TextView) view).setTextSize(20);
                }
                return view;
            }
        };
        listView.setAdapter(adapter);
    }
}
