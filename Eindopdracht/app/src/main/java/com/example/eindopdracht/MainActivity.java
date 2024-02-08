package com.example.eindopdracht;

import androidx.appcompat.app.AppCompatActivity;
import com.example.eindopdracht.ApiCall;
import com.example.eindopdracht.Stock;

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
    Stock AAPL;
    Stock TSLA;
    Stock MSFT;
    Stock NVDA;
    Stock INTC;
    Stock BITCOIN;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CustomListView = this;

        AAPL = new Stock();
        AAPL.setStockName("AAPL");

        TSLA = new Stock();
        TSLA.setStockName("TSLA");

        MSFT = new Stock();
        MSFT.setStockName("MSFT");

        NVDA = new Stock();
        NVDA.setStockName("NVDA");

        INTC = new Stock();
        INTC.setStockName("INTC");

        BITCOIN = new Stock();
        BITCOIN.setStockName("BTC-USD");

        setListData(AAPL);
        setListData(TSLA);
        setListData(MSFT);
        setListData(NVDA);
        setListData(INTC);
        setListData(BITCOIN);

        Resources res =getResources();
        list= ( ListView )findViewById( R.id.list );  // List defined in XML ( See Below )

        /**************** Create Custom Adapter *********/
        adapter=new StockListAdapter( CustomListView, CustomListViewValuesArr,res );
        list.setAdapter( adapter );
    }

    public void updateStockData( Stock stock) {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                api.updateStockPrice(stock);
            }
        });
        t.start();

        try {
            t.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    public String getChangeData(Stock stock) {
        final DecimalFormat df = new DecimalFormat("0.00");
            String input = stock.getPrice();
            float currPrice = Float.parseFloat(input.replaceAll("[^\\d.]", ""));

            float prevPrice = Float.parseFloat(stock.getPrevClose());

            float changed = currPrice - prevPrice;

            return String.format("%.2f", changed);
    }


    /****** Function to set data in ArrayList *************/
    public void setListData(Stock stock)
    {
       // final Stock sched = new Stock();
        updateStockData(stock);
        stock.setStockName(stock.getStockName());
        stock.setPrice("Current Price: $" + stock.getPrice());
        stock.setDaychange("Change since previous close: $" + getChangeData(stock));

        CustomListViewValuesArr.add( stock );

    }
    public void onItemClick(int mPosition)
    {
        Stock tempValues = ( Stock ) CustomListViewValuesArr.get(mPosition);
        /** Send to second screen **/

        Intent myIntent = new Intent(MainActivity.this,
                StockDetail.class);
        myIntent.putExtra("stockObject", tempValues);
        startActivity(myIntent);
    }
}
