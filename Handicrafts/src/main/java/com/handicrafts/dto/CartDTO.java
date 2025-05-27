package com.handicrafts.dto;

import java.text.DecimalFormat;

public class CartDTO {
    private int cartID;
    private int quantity;
    private UserDTO user;
    private HandicraftDTO handicraft;

    public int getCartID() {
        return cartID;
    }

    public void setCartID(int cartID) {
        this.cartID = cartID;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public HandicraftDTO getHandicraft() {
        return handicraft;
    }

    public void setHandicraft(HandicraftDTO handicraft) {
        this.handicraft = handicraft;
    }

    public double getTotalAmount() {
        if (handicraft == null) return 0;
        return this.quantity * handicraft.getPrice() * (1 - handicraft.getDiscountPercent() / 100.0);
    }

    public String getTotalFormat() {
        DecimalFormat df = new DecimalFormat("###,###,###");
        return df.format(getTotalAmount()) + " VND";
    }
}
