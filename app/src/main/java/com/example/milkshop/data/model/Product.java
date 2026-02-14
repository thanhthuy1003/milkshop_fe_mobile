package com.example.milkshop.data.model;

public class Product {
    private int id;
    private String name;
    private String description;
    private int quantity;
    private double originalPrice;
    private double salePrice;
    private String thumbnail;
    private int categoryId;
    private int brandId;
    private int unitId;
    private int statusId;

    public Product() {}

    public Product(int id, String name, String description, int quantity, double originalPrice, double salePrice, String thumbnail, int categoryId, int brandId, int unitId, int statusId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.quantity = quantity;
        this.originalPrice = originalPrice;
        this.salePrice = salePrice;
        this.thumbnail = thumbnail;
        this.categoryId = categoryId;
        this.brandId = brandId;
        this.unitId = unitId;
        this.statusId = statusId;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public double getOriginalPrice() { return originalPrice; }
    public void setOriginalPrice(double originalPrice) { this.originalPrice = originalPrice; }
    public double getSalePrice() { return salePrice; }
    public void setSalePrice(double salePrice) { this.salePrice = salePrice; }
    public String getThumbnail() { return thumbnail; }
    public void setThumbnail(String thumbnail) { this.thumbnail = thumbnail; }
    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }
    public int getBrandId() { return brandId; }
    public void setBrandId(int brandId) { this.brandId = brandId; }
    public int getUnitId() { return unitId; }
    public void setUnitId(int unitId) { this.unitId = unitId; }
    public int getStatusId() { return statusId; }
    public void setStatusId(int statusId) { this.statusId = statusId; }
}
