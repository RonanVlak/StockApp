package com.example.eindopdracht;
import android.util.Log;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

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

    public String updateStockPrice (String symbol, String type) {
        try {
        OkHttpClient client = new OkHttpClient();

        RequestBody body = new FormBody.Builder()
                .add("symbols", symbol)
             //   .add("fields", "regularMarketPrice")
                .build();

        Request request = new Request.Builder()
        //      .url("https://query1.finance.yahoo.com/v10/finance/quote?&symbols=" + symbol + "&fields=regularMarketPrice")
                .url("https://query1.finance.yahoo.com/v8/finance/chart/" + symbol)
                .get()
                .build();

        Response response = null;

            //Gson gson = new Gson();
            try {
                response = client.newCall(request).execute();
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

            if (type == "Price") {
                String regMarketPrice = meta.getString("regularMarketPrice");
                System.out.println(regMarketPrice);
                return regMarketPrice;
            }
            if (type == "Change") {
                String previousClose = meta.getString("previousClose");
                System.out.println(previousClose);
                return previousClose;
            }
            else if (type == "Details") {
                String jsonfile = meta.toString();
                return jsonfile;
            }
        } catch (IOException e) {
            //Error in call
            e.printStackTrace();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return "Something went wrong";
    }

    }
