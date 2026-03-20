package com.example.milkshop.data.model;

import com.google.gson.annotations.SerializedName;

public class Province {
    @SerializedName("ProvinceID")
    private int provinceId;
    
    @SerializedName("ProvinceName")
    private String provinceName;

    public Province(int provinceId, String provinceName) {
        this.provinceId = provinceId;
        this.provinceName = provinceName;
    }

    public int getProvinceId() { return provinceId; }
    public String getProvinceName() { return provinceName; }

    @Override
    public String toString() {
        return provinceName;
    }
}
