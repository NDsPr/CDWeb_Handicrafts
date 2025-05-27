package com.handicrafts.service;

import com.handicrafts.dto.CartDTO;

import java.util.List;

public interface ICartService {
    public CartDTO addProduct(String email, int handicraftId, int quantity);

    public List<CartDTO> getHandicrafts(String email);

    public List<CartDTO> deleteHandicraft(String email, int handicraftId);

    public List<CartDTO> updateQuantity(String email, int handicraftId, int quantity);

    public CartDTO getById(int id);

    public void deleteCart(CartDTO cart);
}
