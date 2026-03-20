package com.example.milkshop.data.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ProductStatsResponse {
    @SerializedName("totalSold")
    private int totalSold;
    
    @SerializedName("bestSellers")
    private List<Product> bestSellers;

    public int getTotalSold() { return totalSold; }
    public List<Product> getBestSellers() { return bestSellers; }
}
