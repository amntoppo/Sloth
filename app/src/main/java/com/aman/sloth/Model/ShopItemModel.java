package com.aman.sloth.Model;

import android.util.Log;

public class ShopItemModel {
    String itemName;
    int price;

    public ShopItemModel() {

    }


    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
    public ShopItemModel(String itemName, int price) {
        this.itemName = itemName;
        this.price = price;
    }
}
