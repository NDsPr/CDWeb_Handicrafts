package com.handicrafts.controller.admin.order;

import com.handicrafts.dto.OrderDTO;
import com.handicrafts.entity.OrderEntity;
import com.handicrafts.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin/order-management")
public class OrderManagementController {

    @Autowired
    private OrderRepository orderRepository;

    @Value("${order.view.management}")
    private String orderManagementView;

    @Value("${order.list.attribute}")
    private String orderListAttribute;

    @GetMapping
    public String showOrderManagementPage(Model model) {
        List<OrderEntity> listOrders = orderRepository.findAll();
        model.addAttribute(orderListAttribute, listOrders);
        return orderManagementView;
    }
}
