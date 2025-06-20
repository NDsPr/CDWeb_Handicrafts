package com.handicrafts.controller.web;

import com.handicrafts.dto.CustomizeDTO;
import com.handicrafts.repository.CustomizeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

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
        Object cart = session.getAttribute("cart");

        model.addAttribute("customizeInfo", customizeInfo);
        model.addAttribute("cart", cart); // Gửi giỏ hàng sang view nếu cần

        return "web/cart"; // Trả về cart.jsp hoặc cart.html (tùy bạn dùng JSP hay Thymeleaf)
    }
}
