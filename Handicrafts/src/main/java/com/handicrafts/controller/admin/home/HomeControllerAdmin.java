package com.handicrafts.controller.admin.home;

import com.handicrafts.repository.UserRepository;
import com.handicrafts.repository.ProductRepository;
import com.handicrafts.repository.OrderRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collections;
import java.util.List;
@Controller
@RequestMapping("/admin")
public class HomeControllerAdmin {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @GetMapping("/home")
    public String home(Model model, HttpServletRequest request) {
        List<Integer> countUser = userRepository.countUser();
        List<Integer> countProduct = Collections.singletonList(productRepository.countProduct());
        List<Integer> countOrder = orderRepository.countOrder();

        model.addAttribute("countProduct", countProduct);
        model.addAttribute("countUser", countUser);
        model.addAttribute("countOrder", countOrder);

        // Thêm HttpServletRequest vào model
        model.addAttribute("currentPage", "home");

        // Đổi tên file từ admin-home thành adding-home
        return "admin/admin-home";
    }
}
