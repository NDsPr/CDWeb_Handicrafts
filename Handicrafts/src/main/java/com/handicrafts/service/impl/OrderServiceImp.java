package com.handicrafts.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.handicrafts.converter.OrderConverter;
import com.handicrafts.entity.OrderEntity;
import com.handicrafts.repository.OrderRepository;
import com.handicrafts.service.IOrderService;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImp implements IOrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderConverter orderConverter;

    @Override
    public void save(OrderDTO order) {
        orderRepository.save(orderConverter.toEntity(order));
    }

    @Override
    public List<OrderDTO> findAllByUserId(int id) {
        List<OrderEntity> list = orderRepository.findAllByUserUserID(id);
        List<OrderDTO> result = new ArrayList<>();
        for (OrderEntity e : list) {
            result.add(orderConverter.toDTO(e));
        }
        return result;
    }

    @Override
    public List<OrderDTO> findAll() {
        List<OrderEntity> list = orderRepository.findAll();
        List<OrderDTO> result = new ArrayList<>();
        for (OrderEntity e : list) {
            result.add(orderConverter.toDTO(e));
        }
        return result;
    }

    @Override
    public void update(OrderDTO order, int id) {
        orderRepository.updateOrder(id, order.getStatus(), order.getNote(), order.getUpdatedAt());
    }

    @Override
    public OrderDTO findById(int id) {
        return orderConverter.toDTO(orderRepository.findByOrderID(id));
    }

    @Override
    public void deleteById(int id) {
        orderRepository.deleteByOrderID(id);
    }

    @Override
    public OrderDTO findLastSave() {
        return orderConverter.toDTO(orderRepository.findFirstByOrderByOrderIDDesc());
    }
}

