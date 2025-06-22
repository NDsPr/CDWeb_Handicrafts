package com.handicrafts.api.cart;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.handicrafts.dto.CartDTO;
import com.handicrafts.dto.ItemDTO;
import com.handicrafts.util.SessionUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/api")
public class CartDeletingAPI {

    @PostMapping("/cart-deleting")
    public ResponseEntity<String> deleteFromCart(@RequestParam("productId") int productId,
                                                 HttpServletRequest request,
                                                 HttpServletResponse response) {
        try {
            System.out.println("CartDeletingAPI called! ProductId: " + productId);

            // Lấy cart từ session
            CartDTO cart = (CartDTO) SessionUtil.getInstance().getValue(request, "cart");

            if (cart == null) {
                System.out.println("Cart is null in session");
                return ResponseEntity.badRequest().body("{\"error\":\"No cart in session\"}");
            }

            // Xóa item
            int status = 0;
            List<ItemDTO> items = cart.getItems();
            if (!items.isEmpty()) {
                status = cart.deleteItem(productId);
            }

            // Cập nhật lại cart trong session
            SessionUtil.getInstance().putValue(request, "cart", cart);

            int totalItems = cart.getTotalItem();

            String serverResponse = switch (status) {
                case -1 -> "fail";
                case 1 -> "success";
                case 0 -> "empty";
                default -> "invalid";
            };

            // Tạo JSON response
            String jsonResponse = String.format(
                    "{\"serverResponse\": \"%s\", \"totalItems\": %d}",
                    serverResponse, totalItems
            );

            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");

            System.out.println("Delete response: " + jsonResponse);
            return ResponseEntity.ok(jsonResponse);

        } catch (Exception e) {
            System.out.println("Error in CartDeletingAPI: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }
}
