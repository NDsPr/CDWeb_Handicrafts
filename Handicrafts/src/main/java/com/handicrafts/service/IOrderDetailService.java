package com.handicrafts.service;

import com.handicrafts.dto.OrderDetailDTO;
import com.handicrafts.entity.OrderDetailId;

import java.util.List;

public interface IOrderDetailService {
    List<OrderDetailDTO> findByOrderId(Integer orderId);
    OrderDetailDTO save(OrderDetailDTO dto);

    void deleteById(Integer id);

    void deleteByOrderId(Integer orderId);
}
