package com.example.eindopdracht;


import java.util.ArrayList;
import java.util.HashMap;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


public class StockListAdapter extends BaseAdapter {

    private Activity activity;
    private ArrayList<Stock> data;
    private LayoutInflater inflater;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public StockListAdapter(Activity activity, ArrayList<Stock> data, OnItemClickListener onItemClickListener) {
        this.activity = activity;
        this.data = data;
        this.onItemClickListener = onItemClickListener;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.tabitem, parent, false);
            holder = new ViewHolder();
            holder.text = convertView.findViewById(R.id.text);
            holder.text1 = convertView.findViewById(R.id.text1);
            holder.text2 = convertView.findViewById(R.id.text2);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Stock stock = data.get(position);
        holder.text.setText(stock.getStockName());
        holder.text1.setText(stock.getPrice());
        holder.text2.setText(stock.getDaychange());


            // Set item click listener
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(position);
                }
            });
        return convertView;
    }

    private static class ViewHolder {
        TextView text;
        TextView text1;
        TextView text2;
    }

}

