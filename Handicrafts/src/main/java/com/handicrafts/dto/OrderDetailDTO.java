package com.handicrafts.dto;

import java.io.Serializable;

public class OrderDetailDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer id;

    private Integer orderId;
    private Integer productId;
    private String productName;
    private Double originalPrice;
    private Double discountPrice;
    private Double discountPercent;
    private Integer quantity;
    private Boolean reviewed;

    public OrderDetailDTO() {
    }

    public OrderDetailDTO(Integer id, Integer orderId, Integer productId, String productName,
                          Double originalPrice, Double discountPrice, Integer discountPercent,
                          Integer quantity, Boolean reviewed) {
        this.id = id;
        this.orderId = orderId;
        this.productId = productId;
        this.productName = productName;
        this.originalPrice = originalPrice;
        this.discountPrice = discountPrice;
        this.discountPercent = discountPercent;
        this.quantity = quantity;
        this.reviewed = reviewed;
    }

    // Constructor that accepts primitive types and converts them
    public OrderDetailDTO(int orderId, int productId, String productName,
                          double originalPrice, double discountPrice, double discountPercent,
                          int quantity, int reviewed) {
        this.orderId = orderId;
        this.productId = productId;
        this.productName = productName;
        this.originalPrice = originalPrice;
        this.discountPrice = discountPrice;
        this.discountPercent = discountPercent;
        this.quantity = quantity;
        this.reviewed = reviewed == 1;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Double getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(Double originalPrice) {
        this.originalPrice = originalPrice;
    }

    public Double getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(Double discountPrice) {
        this.discountPrice = discountPrice;
    }

    public Double getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(Double discountPercent) {
        this.discountPercent = discountPercent;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Boolean getReviewed() {
        return reviewed;
    }

    public void setReviewed(Boolean reviewed) {
        this.reviewed = reviewed;
    }

    // Convenience methods for working with primitive types
    public int getReviewedAsInt() {
        return reviewed ? 1 : 0;
    }

    public void setReviewed(int reviewed) {
        this.reviewed = reviewed == 1;
    }
}
