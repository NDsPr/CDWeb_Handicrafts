package com.handicrafts.controller.web;


import com.handicrafts.api.output.CartOutput;
import com.handicrafts.dto.CartDTO;
import com.handicrafts.dto.HandicraftDTO;
import com.handicrafts.oauth2.CustomOAuth2User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CartController {

    @Autowired
    private ICartService cartService;

    @GetMapping("/them-san-pham")
    public CartDTO addProduct(@RequestParam(name = "handicraftID") int handicraftId,
                              @RequestParam(name = "quantity") int quantity,
                              Authentication authentication) {
        if (authentication != null) {
            String userEmail = getUserEmail(authentication);
            return cartService.addHandicraft(userEmail, handicraftId, quantity);
        }
        CartDTO result = new CartDTO();
        result.setHandicraft(new HandicraftDTO());
        return result;
    }

    @GetMapping("/get-san-pham")
    public CartOutput getProducts(Authentication authentication) {
        if (authentication == null) return null;
        String userEmail = getUserEmail(authentication);

        List<CartDTO> handicraftsDb = cartService.getHandicrafts(userEmail);
        double total = 0.0;
        for (CartDTO c : handicraftsDb) {
            total += c.getHandicraft().getPrice() * (1 - (c.getHandicraft().getDiscountPercent() / 100)) * c.getQuantity();
        }

        CartOutput output = new CartOutput();
        output.setTotal(total);
        output.setCartList(handicraftsDb);
        return output;
    }

    @GetMapping("/xoa-san-pham")
    public CartOutput deleteProduct(@RequestParam(name = "handicraftID") int handicraftId,
                                    Authentication authentication) {
        if (authentication == null) return null;
        String userEmail = getUserEmail(authentication);

        List<CartDTO> cartDeleted = cartService.deleteHandicraft(userEmail, handicraftId);
        double total = 0.0;
        for (CartDTO c : cartDeleted) {
            total += c.getHandicraft().getPrice() * (1 - (c.getHandicraft().getDiscountPercent() / 100)) * c.getQuantity();
        }

        CartOutput outputDelete = new CartOutput();
        outputDelete.setTotal(total);
        outputDelete.setCartList(cartDeleted);
        return outputDelete;
    }

    @GetMapping("/cap-nhat-so-luong")
    public CartOutput updateQuantity(@RequestParam(name = "handicraftID") int handicraftId,
                                     @RequestParam(name = "quantity") int newQuantity,
                                     Authentication authentication) {
        if (authentication == null) return null;
        String userEmail = getUserEmail(authentication);

        List<CartDTO> cartUpdate = cartService.updateQuantity(userEmail, handicraftId, newQuantity);
        double total = 0.0;
        for (CartDTO c : cartUpdate) {
            total += c.getHandicraft().getPrice() * (1 - (c.getHandicraft().getDiscountPercent() / 100)) * c.getQuantity();
        }

        CartOutput outputUpdate = new CartOutput();
        outputUpdate.setTotal(total);
        outputUpdate.setCartList(cartUpdate);
        return outputUpdate;
    }

    private String getUserEmail(Authentication authentication) {
        if (authentication.getPrincipal() instanceof CustomOAuth2User) {
            CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
            return oAuth2User.getAttribute("email");
        }
        return authentication.getName();
    }
}

