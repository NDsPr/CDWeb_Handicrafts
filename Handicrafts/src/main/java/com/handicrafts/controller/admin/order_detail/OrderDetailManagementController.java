package com.handicrafts.controller.admin.order_detail;

import com.handicrafts.dto.OrderDetailDTO;
import com.handicrafts.repository.OrderDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/admin/order-detail-management")
public class OrderDetailManagementController {

    private final OrderDetailRepository orderDetailRepository;
    private final Environment environment;

    @Autowired
    public OrderDetailManagementController(OrderDetailRepository orderDetailRepository, Environment environment) {
        this.orderDetailRepository = orderDetailRepository;
        this.environment = environment;
    }

    // TODO: Thêm orderId lên đầu của trang
    // TODO: Thêm nút thông tin đơn hàng trong trang Đơn hàng cho mỗi một đơn hàng

    @GetMapping
    public String showOrderDetail(@RequestParam("orderId") Integer orderId, Model model) {
        List<OrderDetailDTO> orderDetailList = orderDetailRepository.findOrderDetailByOrderId(orderId);

        model.addAttribute(
                environment.getProperty("order.detail.list.attribute", "orderDetailList"),
                orderDetailList
        );

        model.addAttribute(
                environment.getProperty("order.id.attribute", "orderId"),
                orderId
        );

        return environment.getProperty("order.detail.view", "admin/order/detail");
    }
}
