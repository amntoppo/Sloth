package com.aman.sloth.Model;

import java.util.HashMap;
import java.util.Map;

public class ShopModel {
    private String shopname, description, category, city;

    public ShopModel(Object value) {
//        this.shopName = value.shopname;
//        this.description = value.description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ShopModel(String shopName, String description, String category, String city) {
        this.shopname = shopName;
        this.description = description;
        this.category = category;
        this.city = city;
    }
    public ShopModel() {

    }

    public String getShopName() {
        return shopname;
    }

    public void setShopName(String shopName) {
        this.shopname = shopName;
    }

    public String getCategory() {
        return category;
    }
    public String getCity() { return city; }

    public void setCategory(String category) {
        this.category = category;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("shopname", shopname);
        result.put("description", description);
        result.put("category", category);
        result.put("city", city);
        return result;
    }
}
