package com.aman.sloth.Model;

public class ShopModel {
    private String shopName, description, category;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ShopModel(String shopName, String description, String category) {
        this.shopName = shopName;
        this.description = description;
        this.category = category;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
