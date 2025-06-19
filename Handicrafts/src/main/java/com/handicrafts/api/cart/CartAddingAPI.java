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
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Collections;
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
            @RequestParam("id") int id,
            @RequestParam(value = "quantity", defaultValue = "1") int quantity,
            HttpServletRequest request) throws IOException {

        request.setCharacterEncoding("UTF-8");

        Map<String, Object> response = new HashMap<>();

        ProductDTO product = productDAO.findProductById(id);
        List<ProductImageDTO> images = Collections.singletonList(imageDAO.findOneByProductId(id));

        if (product.getQuantity() <= 0) {
            response.put("status", "error");
            response.put("notify", "Sản phẩm đã hết hàng!");
            return ResponseEntity.ok(response);
        }

        HttpSession session = request.getSession();
        CartDTO cart = (CartDTO) SessionUtil.getInstance().getValue((HttpServletRequest) session, "CART");

        if (cart == null) {
            cart = new CartDTO();
        }

        cart.addItem(new ItemDTO(product, quantity, images.get(0).getLink()));
        SessionUtil.getInstance().putValue((HttpServletRequest) session, "CART", cart);

        response.put("status", "success");
        response.put("notify", "Thêm vào giỏ hàng thành công!");
        response.put("quantity", cart.getTotalItem());

        return ResponseEntity.ok(response);
    }
}
