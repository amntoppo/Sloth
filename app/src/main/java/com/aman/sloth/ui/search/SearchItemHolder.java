package com.aman.sloth.ui.search;

import android.view.View;

import com.aman.sloth.R;
import com.google.android.material.textview.MaterialTextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SearchItemHolder extends RecyclerView.ViewHolder {
    private View mView;
    private MaterialTextView shopNameTextView;
    private MaterialTextView shopItemTextView;
    private MaterialTextView itemPriceTextView;

    public SearchItemHolder(@NonNull View itemView) {
        super(itemView);
        mView = itemView;
        shopNameTextView = mView.findViewById(R.id.textview_search_shop_name);
        shopItemTextView = mView.findViewById(R.id.textview_search_item_name);
        itemPriceTextView = mView.findViewById(R.id.textview_search_price);
    }
    public void setShopNameTextView(String shopname) {
        shopNameTextView.setText(shopname);
    }
    public void setShopItemTextView(String itemName) {
        shopItemTextView.setText(itemName);
    }
    public void setShopPriceTextView(String itemPrice) {
        itemPriceTextView.setText(itemPrice);
    }

    private MaterialTextView shopPriceTextView;



}
