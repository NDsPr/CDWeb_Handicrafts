package com.handicrafts.api.cart;

import com.handicrafts.dto.CartDTO;
import com.handicrafts.dto.ItemDTO;
import com.handicrafts.dto.ProductDTO;
import com.handicrafts.dto.ProductImageDTO;
import com.handicrafts.repository.ImageRepository;
import com.handicrafts.repository.ProductRepository;
import com.handicrafts.util.SessionUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class CartAddingAPI {

    @Autowired
    private ProductRepository productDAO;

    @Autowired
    private ImageRepository imageDAO;

    @PostMapping("/cart-adding")
    public ResponseEntity<Map<String, Object>> addToCart(
            @RequestParam("productId") int productId,
            @RequestParam(value = "quantity", defaultValue = "1") int quantity,
            HttpServletRequest request) throws IOException {

        request.setCharacterEncoding("UTF-8");
        Map<String, Object> response = new HashMap<>();

        try {
            // Lấy thông tin sản phẩm
            ProductDTO product = productDAO.findProductById(productId);

            if (product == null) {
                response.put("success", "false");
                response.put("message", "Sản phẩm không tồn tại!");
                return ResponseEntity.badRequest().body(response);
            }

            if (product.getQuantity() <= 0) {
                response.put("success", "false");
                response.put("message", "Sản phẩm đã hết hàng!");
                return ResponseEntity.badRequest().body(response);
            }

            // Lấy cart từ session
            HttpSession session = request.getSession();
            CartDTO cart = (CartDTO) session.getAttribute("cart");

            if (cart == null) {
                cart = new CartDTO();
            }

            // Tạo ItemDTO mới
            ItemDTO item = new ItemDTO();
            item.setProduct(product);
            item.setQuantity(quantity);
            item.setTotal(product.getOriginalPrice() * quantity);

            // Thiết lập giá có giảm giá
            if (product.getDiscountPrice() > 0) {
                item.setTotalWithDiscount(product.getDiscountPrice() * quantity);
            } else {
                item.setTotalWithDiscount(item.getTotal());
            }

            // Thêm item vào cart
            cart.addItem(item);

            // Lưu cart vào session
            session.setAttribute("cart", cart); //

            // Trả về response thành công
            response.put("success", "true"); //
            response.put("message", "Thêm vào giỏ hàng thành công!");
            response.put("totalItems", cart.getTotalItem()); //

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", "false");
            response.put("message", "Có lỗi xảy ra: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}
