package com.example.eindopdracht;

import android.os.Bundle;
import com.example.eindopdracht.ApiCall;
import androidx.appcompat.app.AppCompatActivity;

public class StockDetail extends AppCompatActivity {
    private volatile String jsonreturn;
    ApiCall api = new ApiCall();
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stocklist);
        String json;
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                jsonreturn = api.updateStockPrice("AAPL", "Details");
            }
        });
        t.start();

        try {
            t.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.print(jsonreturn);
    }
}
