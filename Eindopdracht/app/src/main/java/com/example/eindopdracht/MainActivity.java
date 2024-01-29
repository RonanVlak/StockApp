package com.example.eindopdracht;

import androidx.appcompat.app.AppCompatActivity;
import com.example.eindopdracht.ApiCall;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    ApiCall api = new ApiCall();
    ListView list;
    StockListAdapter adapter;
    public  MainActivity CustomListView = null;
    public  ArrayList<Stock> CustomListViewValuesArr = new ArrayList<Stock>();

    private volatile String StockPrice;
    private volatile String change;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CustomListView = this;

        setListData("AAPL");
        setListData("TSLA");
        setListData("MSFT");
        setListData("NVDA");
        setListData("INTL");


        Resources res =getResources();
        list= ( ListView )findViewById( R.id.list );  // List defined in XML ( See Below )

        /**************** Create Custom Adapter *********/
        adapter=new StockListAdapter( CustomListView, CustomListViewValuesArr,res );
        list.setAdapter( adapter );

    }

    public String getPriceData(String symbol) {

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                StockPrice = api.updateStockPrice(symbol, "Price");
            }
        });
        t.start();

        try {
            t.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return StockPrice;
    }

    public String getChangeData(String symbol) {
        final DecimalFormat df = new DecimalFormat("0.00");

            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    change = api.updateStockPrice(symbol, "Change");
                }
            });
            t.start();

            try {
                t.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            float currPrice = Float.parseFloat(StockPrice);

            float prevPrice = Float.parseFloat(change);

            float changed = currPrice - prevPrice;

            return String.format("%.2f", changed);
    }
    /****** Function to set data in ArrayList *************/
    public void setListData(String symbol)
    {
            final Stock sched = new Stock();

            /******* Firstly take data in model object ******/
            sched.setStockName(symbol);
            sched.setPrice("Current Price: $" + getPriceData(symbol));
            sched.setDaychange("Change since previous close: $" + getChangeData(symbol));

            /******** Take Model Object in ArrayList **********/
            CustomListViewValuesArr.add( sched );

    }
    public void onItemClick(int mPosition)
    {
        Stock tempValues = ( Stock ) CustomListViewValuesArr.get(mPosition);
        /** Send to second screen **/

        Intent myIntent = new Intent(MainActivity.this,
                StockDetail.class);
        startActivity(myIntent);
        // SHOW ALERT
    /*
        Toast.makeText(CustomListView,
                ""+tempValues.getStockName()
                        +" Price:"+tempValues.getPrice()
            +" Url:"+tempValues.getDaychange(),
        Toast.LENGTH_LONG)
                    .show();

     */
    }
}
