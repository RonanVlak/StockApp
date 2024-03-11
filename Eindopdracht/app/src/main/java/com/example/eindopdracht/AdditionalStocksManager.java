package com.example.eindopdracht;

import java.util.ArrayList;

public class AdditionalStocksManager {
    private static AdditionalStocksManager instance;
    private ArrayList<Stock> stockList;

    private AdditionalStocksManager() {
        // Initialize the ArrayList
        stockList = new ArrayList<>();
    }

    public static synchronized AdditionalStocksManager getInstance() {
        if (instance == null) {
            instance = new AdditionalStocksManager();
        }
        return instance;
    }

    public ArrayList<Stock> getStockList() {
        return stockList;
    }

    public void addStock(Stock stock) {
        stockList.add(stock);
    }
    public void removeLastStock() {
        if (!stockList.isEmpty()) {
            stockList.remove(stockList.size() - 1);
        }
    }
}
