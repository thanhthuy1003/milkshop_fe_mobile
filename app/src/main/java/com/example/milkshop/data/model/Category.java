package com.example.milkshop.data.model;

public class Category {
    private int id;
    private String name;
    private String thumbnail;
    private int iconResId;

    public Category(int id, String name, int iconResId) {
        this.id = id;
        this.name = name;
        this.iconResId = iconResId;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getThumbnail() { return thumbnail; }
    public void setThumbnail(String thumbnail) { this.thumbnail = thumbnail; }
    public int getIconResId() { return iconResId; }
}
