package com.handicrafts.service;

import com.handicrafts.dto.CartDTO;

import javax.servlet.http.HttpSession;

public interface ICartService {
    CartDTO getCart(HttpSession session);
    void addToCart(HttpSession session, int productId);
    void updateCart(HttpSession session, int productId, int quantity);
    void removeFromCart(HttpSession session, int productId);
    void clearCart(HttpSession session);
}
