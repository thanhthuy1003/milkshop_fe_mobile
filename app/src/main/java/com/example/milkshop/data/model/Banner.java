package com.example.milkshop.data.model;

import com.google.gson.annotations.SerializedName;

public class Banner {
    @SerializedName("id")
    private String id;
    
    @SerializedName("imageUrl")
    private String imageUrl;
    
    @SerializedName("title")
    private String title;

    public String getId() { return id; }
    public String getImageUrl() { return imageUrl; }
    public String getTitle() { return title; }
}
