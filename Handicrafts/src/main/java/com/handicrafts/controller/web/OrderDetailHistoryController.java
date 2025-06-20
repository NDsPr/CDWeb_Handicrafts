package com.handicrafts.controller.web;

import com.handicrafts.dto.CustomizeDTO;
import com.handicrafts.repository.CustomizeRepository;
import com.handicrafts.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class OrderDetailHistoryController {

    @Autowired
    private CustomizeRepository customizeRepository;

    @Autowired
    private OrderRepository orderRepository;

    @GetMapping("/order-detail-history")
    public String orderDetailHistory(@RequestParam("orderId") int orderId, Model model) {
        CustomizeDTO customizeInfo = customizeRepository.getCustomizeInfo();
        model.addAttribute("customizeInfo", customizeInfo);
        model.addAttribute("orderId", orderId);
        return "web/order-detail-history";
    }
}
