package com.handicrafts.controller.web;

import com.handicrafts.dto.*;
import com.handicrafts.entity.OrderEntity;
import com.handicrafts.repository.OrderDetailRepository;
import com.handicrafts.repository.OrderRepository;
import com.handicrafts.oauth2.CustomOAuth2User;
import com.handicrafts.service.IUserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private IUserService userService;

    @GetMapping("/cart")
    public ModelAndView showCart(Authentication authentication, HttpSession session) {
        ModelAndView mav = new ModelAndView("web/cart");

        // Lấy giỏ hàng từ session
        CartDTO cartDTO = (CartDTO) session.getAttribute("cart");
        if (cartDTO == null) {
            cartDTO = new CartDTO();
            session.setAttribute("cart", cartDTO);
        }

        mav.addObject("cart", cartDTO);
        return mav;
    }


    // @PostMapping("/checkout")
   @PostMapping("/process-order") // Đổi URL để tránh xung đột
   public ModelAndView checkout(@RequestParam(defaultValue = "COD") String paymentMethod,
                                 Authentication authentication) {
        ModelAndView mav = new ModelAndView("redirect:/order-success");

        if (authentication == null || !authentication.isAuthenticated()) {
            return new ModelAndView("redirect:/login");
        }

        int userId = getUserId(authentication);
        List<OrderEntity> userOrders = orderRepository.findOrderByUserId(userId);
        OrderEntity cart = userOrders.stream()
                .filter(order -> order.getStatus() == 1)
                .findFirst()
                .orElse(null);

        if (cart == null) {
            return new ModelAndView("redirect:/cart?error=nocart");
        }

        List<OrderDetailDTO> details = orderDetailRepository.findOrderDetailByOrderId(cart.getId());
        double total = 0;

        for (OrderDetailDTO detail : details) {
            ProductDTO product = detail.getProduct();
            double price = 0;
            if (product != null) {
                price = product.getDiscountPrice();
            } else if (detail.getDiscountPrice() != null) {
                price = detail.getDiscountPrice();
            }
            total += price * detail.getQuantity();
        }

        // cập nhật thông tin đơn hàng
        cart.setStatus(2);
        cart.setPaymentMethod(paymentMethod);
        cart.setShipToDate(Timestamp.valueOf(LocalDateTime.now().plusDays(5)));
        cart.setModifiedDate(Timestamp.valueOf(LocalDateTime.now()));
        cart.setModifiedBy(authentication.getName());
        cart.setTotal(total);

        int affected = orderRepository.updateOrder(cart);
        if (affected <= 0) {
            return new ModelAndView("redirect:/cart?error=checkout");
        }

        return mav;
    }

    @GetMapping("/order-success")
    public ModelAndView orderSuccess() {
        return new ModelAndView("web/order-success");
    }

    private int getUserId(Authentication authentication) {
        if (authentication.getPrincipal() instanceof CustomOAuth2User oauthUser) {
            return userService.findByEmail(oauthUser.getEmail()).getId();
        } else {
            return userService.findByUsername(authentication.getName()).getId();
        }
    }
}
