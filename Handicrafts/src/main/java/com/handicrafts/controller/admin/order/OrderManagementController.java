package com.handicrafts.controller.admin.order;

import com.handicrafts.entity.OrderEntity;
import com.handicrafts.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
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

    @Autowired
    private Environment environment;

    @GetMapping
    public String showOrderManagementPage(Model model) {
        List<OrderEntity> listOrders = orderRepository.findAll();
        model.addAttribute(
                environment.getProperty("order.list.attribute", "listOrders"),
                listOrders
        );
        return environment.getProperty("order.view.management", "admin/order/management");
    }
}
