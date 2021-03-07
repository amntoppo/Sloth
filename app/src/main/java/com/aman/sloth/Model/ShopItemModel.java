package com.aman.sloth.Model;

import android.util.Log;

public class ShopItemModel {
    String itemName;
    String shopID;
    int price;

    public ShopItemModel() {

    }


    public String getItemName() {
        return itemName;
    }
    public String getSHopID() {return shopID;}
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
    public void setshopID(String shopID) { this.shopID = shopID; }
    public ShopItemModel(String itemName, int price, String shopID) {
        this.itemName = itemName;
        this.price = price;
        this.shopID = shopID;
    }
}
