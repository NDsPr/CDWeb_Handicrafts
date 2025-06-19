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
            @RequestParam("id") int id,
            @RequestParam(value = "quantity", defaultValue = "1") int quantity,
            HttpServletRequest request) throws IOException {

        request.setCharacterEncoding("UTF-8");

        Map<String, Object> response = new HashMap<>();

        ProductDTO product = productDAO.findProductById(id);

        // Sửa lại phần lấy hình ảnh
        List<ProductImageDTO> images = imageDAO.findImagesByProductId(id);
        String imageLink = "";
        if (images != null && !images.isEmpty()) {
            imageLink = images.get(0).getLink();
        }

        if (product == null) {
            response.put("status", "error");
            response.put("notify", "Sản phẩm không tồn tại!");
            return ResponseEntity.ok(response);
        }

        if (product.getQuantity() <= 0) {
            response.put("status", "error");
            response.put("notify", "Sản phẩm đã hết hàng!");
            return ResponseEntity.ok(response);
        }

        HttpSession session = request.getSession();
        CartDTO cart = (CartDTO) SessionUtil.getInstance().getValue(request, "CART");

        if (cart == null) {
            cart = new CartDTO();
        }

        // Tạo ItemDTO mới
// Vì constructor không thực hiện gì, nên tốt hơn là dùng constructor mặc định
        ItemDTO item = new ItemDTO();

// Thiết lập các giá trị thủ công
        item.setProduct(product);
        item.setQuantity(quantity);
        item.setTotal(product.getOriginalPrice() * quantity);

// Kiểm tra và thiết lập giá có giảm giá
// Với kiểu double, cần kiểm tra giá trị khác 0 hoặc dùng một giá trị đặc biệt để biểu thị không giảm giá
        if (product.getDiscountPrice() > 0) {
            item.setTotalWithDiscount(product.getDiscountPrice() * quantity);
        } else {
            item.setTotalWithDiscount(item.getTotal());
        }

        cart.addItem(item);
        SessionUtil.getInstance().setValue(request, "CART", cart);



        response.put("status", "success");
        response.put("notify", "Thêm vào giỏ hàng thành công!");
        response.put("quantity", cart.getTotalItem());

        return ResponseEntity.ok(response);
    }
}
