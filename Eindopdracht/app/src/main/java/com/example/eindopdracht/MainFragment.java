package com.example.eindopdracht;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import com.example.eindopdracht.ApiCall;
import com.example.eindopdracht.R;
import com.example.eindopdracht.SettingsActivity;
import com.example.eindopdracht.Stock;
import com.example.eindopdracht.StockListAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class MainFragment extends Fragment {
    private ApiCall api;
    private ListView list;
    private StockListAdapter adapter;
    private ArrayList<Stock> customListViewValuesArr = new ArrayList<Stock>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        api = new ApiCall();
        customListViewValuesArr = new ArrayList<>();

        Stock aapl = new Stock();
        aapl.setStockName("AAPL");

        Stock tsla = new Stock();
        tsla.setStockName("TSLA");

        Stock msft = new Stock();
        msft.setStockName("MSFT");

        Stock nvda = new Stock();
        nvda.setStockName("NVDA");

        Stock intc = new Stock();
        intc.setStockName("INTC");

        Stock bitcoin = new Stock();
        bitcoin.setStockName("BTC-USD");

        updateStockData(aapl);
        updateStockData(tsla);
        updateStockData(msft);
        updateStockData(nvda);
        updateStockData(intc);
        updateStockData(bitcoin);

        for (Stock stock : AdditionalStocksManager.getInstance().getStockList()) {
            updateStockData(stock);
        }

        Resources res = getResources();
        list = rootView.findViewById(R.id.list);

        adapter = new StockListAdapter(requireActivity(), customListViewValuesArr, new StockListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int mPosition) {
                Stock tempValues = customListViewValuesArr.get(mPosition);

                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("stockObject", tempValues);

                    StockDetailFragment detailFragment = new StockDetailFragment();
                    detailFragment.setArguments(bundle);

                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.stock_detail_fragment_container, detailFragment)
                            .addToBackStack(null)
                            .commit();
                } else {
                    Intent myIntent = new Intent(requireContext(), StockDetail.class);
                    myIntent.putExtra("stockObject", tempValues);
                    startActivity(myIntent);
                }
            }
        });
        list.setAdapter(adapter);

        View activityLayout = getActivity().findViewById(android.R.id.content);

        Button addStockButton = activityLayout.findViewById(R.id.addStockButton);
        addStockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the click event to add a new stock
                addNewStock();
            }
        });

        return rootView;
    }

    private void addNewStock() {
        // Example: Show a dialog to input stock symbol
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Add Stock");

        // Set up the input
        final EditText input = new EditText(requireContext());
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String symbol = input.getText().toString();
                Stock newStock = new Stock();
                newStock.setStockName(symbol);
                updateStockData(newStock);
                AdditionalStocksManager.getInstance().addStock(newStock);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void updateStockData(final Stock stock) {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                api.updateStockPrice(stock);
                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setListData(stock);
                    }
                });
            }
        });
        t.start();
    }

    private String getChangeData(Stock stock) {
        DecimalFormat df = new DecimalFormat("0.00");
        String input = stock.getPrice();
        String prevClose = stock.getPrevClose();

        // Check if input strings are not empty or null
        if (!TextUtils.isEmpty(input) && !TextUtils.isEmpty(prevClose)) {
            try {
                float currPrice = Float.parseFloat(input.replaceAll("[^\\d.]", ""));
                float prevPrice = Float.parseFloat(prevClose);
                float changed = currPrice - prevPrice;
                return df.format(changed);
            } catch (NumberFormatException e) {
                Log.e("MainFragment", "NumberFormatException: " + e.getMessage());
                return "N/A"; // Return a default value
            }
        } else {
            return "N/A"; // Return a default value
        }
    }

    private void setListData(Stock stock) {
        if (isAdded()) {
            if (stock.getPrice() != null && !stock.getPrice().isEmpty()) {
                double price = Double.parseDouble(stock.getPrice());

                DecimalFormat df = new DecimalFormat("#.##");
                String formattedPrice = df.format(price);

                stock.setPrice("Current Price: $" + formattedPrice);
                stock.setDaychange("Change since previous close: $" + getChangeData(stock));
                customListViewValuesArr.add(stock);
                adapter.notifyDataSetChanged();
            } else {
                Toast.makeText(requireContext(), "Invalid stock input", Toast.LENGTH_SHORT).show();
                AdditionalStocksManager.getInstance().removeLastStock();
            }
        }
    }
}