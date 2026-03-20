package com.example.milkshop.data.model;

import com.google.gson.annotations.SerializedName;

public class OrderStatsResponse {
    @SerializedName("totalOrders")
    private int totalOrders;

    @SerializedName("totalDelivered")
    private int totalDelivered;

    @SerializedName("totalRevenue")
    private double totalRevenue;

    @SerializedName("totalShippingFee")
    private double totalShippingFee;

    public int getTotalOrders() { return totalOrders; }
    public int getTotalDelivered() { return totalDelivered; }
    public double getTotalRevenue() { return totalRevenue; }
    public double getTotalShippingFee() { return totalShippingFee; }
}
