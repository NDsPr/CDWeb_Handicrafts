package com.handicrafts.dto;

public class OrderDetailDTO {
    private int orderId;
    private int productId;
    private String productName;
    private double originalPrice;
    private double discountPrice;
    private double discountPercent;
    private int quantity;
    private int reviewed;

    public OrderDetailDTO() {
    }

    public OrderDetailDTO(int orderId, int productId, String productName, double originalPrice, double discountPrice, double discountPercent, int quantity, int reviewed) {
        this.orderId = orderId;
        this.productId = productId;
        this.productName = productName;
        this.originalPrice = originalPrice;
        this.discountPrice = discountPrice;
        this.discountPercent = discountPercent;
        this.quantity = quantity;
        this.reviewed = reviewed;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(double originalPrice) {
        this.originalPrice = originalPrice;
    }

    public double getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(double discountPrice) {
        this.discountPrice = discountPrice;
    }

    public double getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(double discountPercent) {
        this.discountPercent = discountPercent;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getReviewed() {
        return reviewed;
    }

    public void setReviewed(int reviewed) {
        this.reviewed = reviewed;
    }
}