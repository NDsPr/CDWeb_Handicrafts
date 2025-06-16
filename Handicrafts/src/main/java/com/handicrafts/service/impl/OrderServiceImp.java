package com.handicrafts.service.impl;

import com.handicrafts.dto.OrderDTO;
import com.handicrafts.entity.OrderEntity;
import com.handicrafts.repository.OrderRepository;
import com.handicrafts.repository.UserRepository;
import com.handicrafts.service.IOrderService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderServiceImp implements IOrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    @Autowired
    public OrderServiceImp(OrderRepository orderRepository,
                            UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
    }

    @Override
    public OrderDTO createOrder(OrderDTO dto) {
        OrderEntity entity = dto.toEntity();
        entity.setCreatedDate(Timestamp.valueOf(LocalDateTime.now()));
        entity.setStatus(1); // default cart
        entity.setCreatedBy(dto.getCreatedBy());

        OrderEntity saved = orderRepository.save(entity);
        return new OrderDTO(saved);
    }

    @Override
    public OrderDTO updateOrder(OrderDTO dto) {
        OrderEntity entity = dto.toEntity();
        entity.setModifiedDate(Timestamp.valueOf(LocalDateTime.now()));
        entity.setModifiedBy(dto.getModifiedBy());

        OrderEntity saved = orderRepository.save(entity);
        return new OrderDTO(saved);
    }

    @Override
    public Optional<OrderDTO> getOrderById(Integer id) {
        return orderRepository.findById(id).map(OrderDTO::new);
    }

    @Override
    public List<OrderDTO> getOrdersByUserId(Integer userId) {
        return orderRepository.findOrderByUserId(userId)
                .stream()
                .map(OrderDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderDTO> getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(OrderDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void deleteOrder(Integer id) {
        orderRepository.deleteById(id);
    }

    @Transactional
    @Override
    public boolean checkoutOrder(Integer userId, String paymentMethod, String modifiedBy) {
        List<OrderDTO> orders = getOrdersByUserId(userId);
        Optional<OrderDTO> cartOpt = orders.stream()
                .filter(o -> o.getStatus() == 1)
                .findFirst();

        if (cartOpt.isEmpty()) return false;

        OrderDTO cart = cartOpt.get();
        cart.setStatus(2); // 2 = đã đặt hàng
        cart.setPaymentMethod(paymentMethod);
        cart.setShipToDate(Timestamp.valueOf(LocalDateTime.now().plusDays(5)));
        cart.setModifiedDate(Timestamp.valueOf(LocalDateTime.now()));
        cart.setModifiedBy(modifiedBy);

        return orderRepository.updateOrder(cart) > 0;
    }

    @Override
    public OrderDTO convertToDTO(OrderEntity entity) {
        return new OrderDTO(entity);
    }
}
