package com.handicrafts.service.impl;

import com.handicrafts.dto.CartDTO;
import com.handicrafts.dto.ItemDTO;
import com.handicrafts.dto.ProductDTO;
import com.handicrafts.service.ICartService;
import com.handicrafts.service.IProductService;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Optional;

@Service
public class CartServiceImp implements ICartService {

    private final IProductService productService;

    public CartServiceImp(IProductService productService) {
        this.productService = productService;
    }

    @Override
    public CartDTO getCart(HttpSession session) {
        CartDTO cart = (CartDTO) session.getAttribute("cart");
        if (cart == null) {
            cart = new CartDTO();
            session.setAttribute("cart", cart);
        }
        return cart;
    }

    @Override
    public void addToCart(HttpSession session, int productId) {
        CartDTO cart = getCart(session);
        ProductDTO product = productService.findById(productId);
        ItemDTO item = new ItemDTO();
        item.setProduct(product);
        cart.addItem(item);
    }

    @Override
    public void updateCart(HttpSession session, int productId, int quantity) {
        CartDTO cart = getCart(session);
        cart.updateItem(productId, quantity);
    }

    @Override
    public void removeFromCart(HttpSession session, int productId) {
        CartDTO cart = getCart(session);
        cart.deleteItem(productId);
    }

    @Override
    public void clearCart(HttpSession session) {
        session.removeAttribute("cart");
    }
}
