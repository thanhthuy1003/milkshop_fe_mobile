package com.example.milkshop.data.model;

public class Category {
    private int id;
    private String name;
    private int iconResId; // For local icons if needed, or use image URL

    public Category(int id, String name, int iconResId) {
        this.id = id;
        this.name = name;
        this.iconResId = iconResId;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public int getIconResId() { return iconResId; }
}
