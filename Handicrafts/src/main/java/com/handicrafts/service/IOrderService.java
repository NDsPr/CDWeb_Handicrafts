package com.handicrafts.service;

import com.handicrafts.dto.OrderDTO;
import com.handicrafts.entity.OrderEntity;

import java.util.List;
import java.util.Optional;

public interface IOrderService {

    // Tạo đơn hàng mới (cart hoặc order)
    OrderDTO createOrder(OrderDTO dto);

    // Cập nhật đơn hàng (thay đổi thông tin, thanh toán, v.v.)
    OrderDTO updateOrder(OrderDTO dto);

    // Lấy đơn hàng theo ID
    Optional<OrderDTO> getOrderById(Integer id);

    // Lấy danh sách đơn hàng theo người dùng
    List<OrderDTO> getOrdersByUserId(Integer userId);

    // Lấy toàn bộ đơn hàng (admin)
    List<OrderDTO> getAllOrders();

    // Xoá đơn hàng theo ID
    void deleteOrder(Integer id);

    // Xử lý thanh toán đơn hàng (thay đổi status, ngày giao hàng...)
    boolean checkoutOrder(Integer userId, String paymentMethod, String modifiedBy);

    // Chuyển từ entity sang DTO
    OrderDTO convertToDTO(OrderEntity entity);
}
