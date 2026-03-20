package com.example.milkshop.data.model;

import com.google.gson.annotations.SerializedName;

public class Ward {
    @SerializedName("WardCode")
    private String wardCode;
    
    @SerializedName("WardName")
    private String wardName;

    public Ward(String wardCode, String wardName) {
        this.wardCode = wardCode;
        this.wardName = wardName;
    }

    public String getWardCode() { return wardCode; }
    public String getWardName() { return wardName; }

    @Override
    public String toString() {
        return wardName;
    }
}
