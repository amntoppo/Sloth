package com.aman.sloth.ui.shop;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.aman.sloth.Model.ShopItemModel;
import com.aman.sloth.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;

public class ListViewAdapter  extends BaseAdapter {

    private Activity mContext;
    private ArrayList<ShopItemModel> items;
    LayoutInflater inflater;

    private TextView itemNameTextView;
    private TextView itemPriceTextView;

    public ListViewAdapter(Activity mContext, ArrayList<ShopItemModel> items) {
        this.mContext = mContext;
        this.items = items;
    }



    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(inflater == null) {
            inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if(convertView == null) {
            convertView = inflater.inflate(R.layout.listview_shop_items, parent, false);
        }
        itemNameTextView = convertView.findViewById(R.id.textview_item_name);
        itemPriceTextView = convertView.findViewById(R.id.textview_item_price);
        Log.e("adapter", items.get(0).getItemName());
        itemNameTextView.setText(items.get(position).getItemName());
        itemPriceTextView.setText(String.valueOf(items.get(position).getPrice()));

        return convertView;
    }
}
