package com.example.milkshop.data.model;

import com.google.gson.annotations.SerializedName;

public class AddToCartModel {
    @SerializedName("productId")
    private String productId;

    @SerializedName("quantity")
    private int quantity;

    public AddToCartModel(String productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public String getProductId() { return productId; }
    public int getQuantity() { return quantity; }
}
