package com.handicrafts.controller.web;

import com.handicrafts.dto.CartDTO;
import com.handicrafts.dto.CustomizeDTO;
import com.handicrafts.repository.CustomizeRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class CartManagementController {

    private final CustomizeRepository customizeRepository;

    @Autowired
    public CartManagementController(CustomizeRepository customizeRepository) {
        this.customizeRepository = customizeRepository;
    }

    @GetMapping("/cart-management")
    public String showCartPage(Model model, HttpSession session) {
        CustomizeDTO customizeInfo = customizeRepository.getCustomizeInfo();

        // Lấy giỏ hàng từ session
        Object cartObj = session.getAttribute("cart");

        // Đảm bảo luôn có một đối tượng Cart hợp lệ
        CartDTO cart;
        if (cartObj == null) {
            cart = new CartDTO(); // Đảm bảo lớp Cart của bạn có constructor không tham số
            session.setAttribute("cart", cart);
        } else {
            try {
                cart = (CartDTO) cartObj;
            } catch (ClassCastException e) {
                // Xử lý trường hợp đối tượng trong session không phải là Cart
                cart = new CartDTO();
                session.setAttribute("cart", cart);
            }
        }

        model.addAttribute("customizeInfo", customizeInfo);
        model.addAttribute("cart", cart);

        return "web/cart";
    }
}
