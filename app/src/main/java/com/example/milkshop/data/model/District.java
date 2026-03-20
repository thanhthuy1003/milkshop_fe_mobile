package com.example.milkshop.data.model;

import com.google.gson.annotations.SerializedName;

public class District {
    @SerializedName("DistrictID")
    private int districtId;
    
    @SerializedName("DistrictName")
    private String districtName;

    public District(int districtId, String districtName) {
        this.districtId = districtId;
        this.districtName = districtName;
    }

    public int getDistrictId() { return districtId; }
    public String getDistrictName() { return districtName; }

    @Override
    public String toString() {
        return districtName;
    }
}
