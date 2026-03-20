package com.example.milkshop.data.model;

import com.google.gson.annotations.SerializedName;

public class UpdateCartItemModel {
    @SerializedName("quantity")
    private int quantity;

    public UpdateCartItemModel(int quantity) {
        this.quantity = quantity;
    }

    public int getQuantity() { return quantity; }
}
