package com.example.milkshop.data.model;

/**
 * Model đại diện cho một sản phẩm trong giỏ hàng.
 */
public class CartItem {
    private int productId;
    private String productName;
    private String productThumbnail;
    private double price;
    private int quantity;

    public CartItem(int productId, String productName, String productThumbnail, double price, int quantity) {
        this.productId = productId;
        this.productName = productName;
        this.productThumbnail = productThumbnail;
        this.price = price;
        this.quantity = quantity;
    }

    public int getProductId() { return productId; }
    public String getProductName() { return productName; }
    public String getProductThumbnail() { return productThumbnail; }
    public double getPrice() { return price; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}
