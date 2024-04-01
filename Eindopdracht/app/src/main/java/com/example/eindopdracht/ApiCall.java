package com.example.eindopdracht;
import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import com.example.eindopdracht.Stock;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ApiCall extends Thread {
    public ApiCall () {

    }

    public Stock updateStockPrice(Stock stock) {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url("https://query1.finance.yahoo.com/v8/finance/chart/" + stock.getStockName())
                    .get()
                    .build();

            try {
                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    processResponse(response.body().string(), stock);
                } else {
                    Log.e("ApiCall", "Failed to fetch stock data. HTTP status code: " + response.code());
                    // Handle unsuccessful response
                }
            } catch (IOException e) {
                Log.e("ApiCall", "Error fetching stock data: " + e.getMessage());
                // Handle network or I/O error
            }
        return stock;
    }

    private Stock processResponse(String jsonData, Stock stock) {
        try {
            JSONObject jObject = new JSONObject(jsonData);
            JSONObject chart = jObject.getJSONObject("chart");
            JSONArray result = chart.getJSONArray("result");
            JSONObject zero = result.getJSONObject(0);
            JSONObject meta = zero.getJSONObject("meta");

            String regularMarketPrice = meta.optString("regularMarketPrice", null);
            String previousClose = meta.optString("previousClose", null);
            String currency = meta.optString("currency", null);
            String exchangeName = meta.optString("exchangeName", null);
            String instrumentType = meta.optString("instrumentType", null);

            // Check if all required fields are present
            if (regularMarketPrice != null && previousClose != null && currency != null && exchangeName != null && instrumentType != null) {
                stock.setPrice(regularMarketPrice);
                stock.setPrevClose(previousClose);
                stock.setCurrency(currency);
                stock.setExchangeName(exchangeName);
                stock.setInstrumentType(instrumentType);
            } else {
                // Handle the case when one or more fields are missing
                Log.e("ApiCall", "One or more fields missing in JSON response");
            }
        } catch (JSONException e) {
            // Error parsing JSON
            e.printStackTrace();
        }
        return stock;
    }
}