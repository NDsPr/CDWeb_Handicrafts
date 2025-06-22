package com.handicrafts.api.cart;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.handicrafts.dto.CartDTO;
import com.handicrafts.util.SessionUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api")
public class CartUpdatingAPI {


    @PostMapping("/cart-updating")
    public ResponseEntity<String> updateCart(@RequestParam("productId") int productId,
                                             @RequestParam("quantity") int quantity,
                                             HttpServletRequest request,
                                             HttpServletResponse response) {
        try {
            System.out.println("Received request - ProductId: " + productId + ", Quantity: " + quantity);

            // Lấy cart từ session
            CartDTO cart = (CartDTO) SessionUtil.getInstance().getValue(request, "cart");

            if (cart == null) {
                System.out.println("Cart is null in session");
                return ResponseEntity.badRequest().body("{\"error\":\"Cart not found in session\"}");
            }

            cart.updateItem(productId, quantity);

            // Cập nhật lại cart trong session
            SessionUtil.getInstance().putValue(request, "cart", cart);

            // Trả về JSON cập nhật
            ObjectMapper objectMapper = new ObjectMapper();
            String cartJson = objectMapper.writeValueAsString(cart);

            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");

//            System.out.println("Response: " + cartJson);
            return ResponseEntity.ok(cartJson);

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }
}
