package com.handicrafts.api.cart;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.handicrafts.dto.CartDTO;
import com.handicrafts.util.SessionUtil;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/cart-updating")
public class CartUpdatingAPI {

    @PostMapping
    public String updateCart(@RequestParam int productId,
                             @RequestParam int quantity,
                             HttpServletRequest request,
                             HttpServletResponse response) throws JsonProcessingException {
        // Lấy cart từ session
        CartDTO cart = (CartDTO) SessionUtil.getInstance().getValue((jakarta.servlet.http.HttpServletRequest) request, "cart");

        if (cart != null) {
            cart.updateItem(productId, quantity);
        }

        // Trả về JSON cập nhật
        ObjectMapper objectMapper = new ObjectMapper();
        String cartJson = objectMapper.writeValueAsString(cart);

        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        return cartJson;
    }
}
