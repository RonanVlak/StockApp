package com.example.eindopdracht;
import android.util.Log;
import com.google.gson.Gson;
import java.io.IOException;
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

    public Stock updateStockPrice (Stock stock) {
        try {
            OkHttpClient client = new OkHttpClient();

            RequestBody body = new FormBody.Builder()
                    .add("symbols", stock.getStockName())
                    .build();

            Request request = new Request.Builder()
                    .url("https://query1.finance.yahoo.com/v8/finance/chart/" + stock.getStockName())
                    .get()
                    .build();

            Response response = null;


            try {
                response = client.newCall(request).execute();
                if(!response.isSuccessful()) {
                    return null;
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            String jsonData = response.body().string();
            System.out.println(jsonData);
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

            } catch (IOException | JSONException e) {
            //Error in call
            e.printStackTrace();
        }
        return stock;
    }

}