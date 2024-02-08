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
    Stock AAPL = new Stock();
    AAPL.SetStockName("AAPL");
    Stock TSLA = new Stock();
    TSLA.SetStockName("TSLA");
    Stock MSFT = new Stock();
    MSFT.SetStockName("MSFT");
    Stock NVDA = new Stock();
    NVDA.SetStockName("NVDA");
    Stock INTL = new Stock();
    INTL.SetStockName("INTL");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CustomListView = this;
        /*
        setListData("AAPL");
        setListData("TSLA");
        setListData("MSFT");
        setListData("NVDA");
        setListData("INTL");
        */

        setListData(AAPL);

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
    /*
    public void setListData(String symbol)
    {
            final Stock sched = new Stock();

            sched.setStockName(symbol);
            sched.setPrice("Current Price: $" + getPriceData(symbol));
            sched.setDaychange("Change since previous close: $" + getChangeData(symbol));

            CustomListViewValuesArr.add( sched );

    }
    */
    public void setListData(Stock stock)
    {
       // final Stock sched = new Stock();

        stock.setStockName(stock.getStockName());
        stock.setPrice("Current Price: $" + getPriceData(stock.getStockName()));
        stock.setDaychange("Change since previous close: $" + getChangeData(stock.getStockName()));

        CustomListViewValuesArr.add( stock );

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
