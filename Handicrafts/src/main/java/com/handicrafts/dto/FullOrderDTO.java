package com.handicrafts.dto;

import com.handicrafts.entity.OrderEntity;

import java.util.List;

public class FullOrderDTO {
    private OrderEntity order;
    private List<OrderDetailDTO> orderDetails;

    public FullOrderDTO() {}

    public FullOrderDTO(OrderEntity order, List<OrderDetailDTO> orderDetails) {
        this.order = order;
        this.orderDetails = orderDetails;
    }

    public OrderEntity getOrder() {
        return order;
    }

    public void setOrder(OrderEntity order) {
        this.order = order;
    }

    public List<OrderDetailDTO> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetailDTO> orderDetails) {
        this.orderDetails = orderDetails;
    }
}
