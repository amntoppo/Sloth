package com.aman.sloth.Model;

import java.util.HashMap;
import java.util.Map;

public class ShopModel {
    private String shopName, description, category, city;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ShopModel(String shopName, String description, String category, String city) {
        this.shopName = shopName;
        this.description = description;
        this.category = category;
        this.city = city;
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

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("shopname", shopName);
        result.put("description", description);
        result.put("category", category);
        result.put("city", city);
        return result;
    }
}
