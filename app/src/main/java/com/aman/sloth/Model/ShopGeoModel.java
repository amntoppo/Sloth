package com.aman.sloth.Model;

import com.firebase.geofire.GeoLocation;

public class ShopGeoModel {
    private String key;
    private GeoLocation geoLocation;
    private ShopModel shopModel;

    public ShopGeoModel(String key, GeoLocation geoLocation, ShopModel shopModel) {
        this.key = key;
        this.geoLocation = geoLocation;
        this.shopModel = shopModel;
    }
    public ShopGeoModel(String key, GeoLocation geoLocation) {
        this.key = key;
        this.geoLocation = geoLocation;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public GeoLocation getGeoLocation() {
        return geoLocation;
    }

    public void setGeoLocation(GeoLocation geoLocation) {
        this.geoLocation = geoLocation;
    }

    public ShopModel getShopModel() {
        return shopModel;
    }

    public void setShopModel(ShopModel shopModel) {
        this.shopModel = shopModel;
    }
}
