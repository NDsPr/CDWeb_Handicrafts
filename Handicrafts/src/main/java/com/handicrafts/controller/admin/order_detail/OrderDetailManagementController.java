package com.handicrafts.controller.admin.order_detail;

import com.handicrafts.dto.OrderDetailDTO;
import com.handicrafts.repository.OrderDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/admin/order-detail-management")
@RequiredArgsConstructor
public class OrderDetailManagementController {

    private final OrderDetailRepository orderDetailRepository;

    @Value("${order.detail.view}")
    private String orderDetailView;

    @Value("${order.detail.list.attribute}")
    private String orderDetailListAttribute;

    @Value("${order.id.attribute}")
    private String orderIdAttribute;

    // TODO: Thêm orderId lên đầu của trang
    // TODO: Thêm nút thông tin đơn hàng trong trang Đơn hàng cho mỗi một đơn hàng

    @GetMapping
    public String showOrderDetail(@RequestParam("orderId") Integer orderId, Model model) {
        List<OrderDetailDTO> orderDetailList = orderDetailRepository.findOrderDetailByOrderId(orderId);
        model.addAttribute(orderDetailListAttribute, orderDetailList);
        model.addAttribute(orderIdAttribute, orderId); // Đưa orderId lên đầu trang (nếu cần)
        return orderDetailView; // Tên view Thymeleaf
    }
}
