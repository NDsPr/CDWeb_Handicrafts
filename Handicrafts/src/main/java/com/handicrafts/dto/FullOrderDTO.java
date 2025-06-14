package com.handicrafts.dto;

import com.ltw.bean.OrderBean;
import com.ltw.bean.OrderDetailBean;

import java.util.List;

public class FullOrderDTO {
    private OrderBean order;
    private List<OrderDetailBean> orderDetails;

    public FullOrderDTO() {}

    public FullOrderDTO(OrderBean order, List<OrderDetailBean> orderDetails) {
        this.order = order;
        this.orderDetails = orderDetails;
    }

    public OrderBean getOrder() {
        return order;
    }

    public void setOrder(OrderBean order) {
        this.order = order;
    }

    public List<OrderDetailBean> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetailBean> orderDetails) {
        this.orderDetails = orderDetails;
    }
}
