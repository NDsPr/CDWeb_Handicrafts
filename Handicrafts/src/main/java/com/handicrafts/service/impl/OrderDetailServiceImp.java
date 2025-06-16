package com.handicrafts.service.impl;

import com.handicrafts.dto.OrderDetailDTO;
import com.handicrafts.entity.OrderDetailEntity;
import com.handicrafts.entity.OrderDetailId;
import com.handicrafts.repository.OrderDetailRepository;
import com.handicrafts.service.IOrderDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderDetailServiceImp implements IOrderDetailService {

    @Autowired
    private OrderDetailRepository repository;

    @Override
    public List<OrderDetailDTO> findByOrderId(Integer orderId) {
        return repository.findByOrderId(orderId)
                .stream()
                .map(entity -> new OrderDetailDTO(
                        entity.getId(),
                        entity.getOrderId(),
                        entity.getProductId(),
                        entity.getProduct() != null ? entity.getProduct().getName() : null,
                        entity.getProduct() != null ? entity.getProduct().getOriginalPrice() : null,
                        entity.getProduct() != null ? entity.getProduct().getDiscountPrice() : null,
                        entity.getProduct() != null ? entity.getProduct().getDiscountPercent() : null,
                        entity.getQuantity(),
                        entity.getReviewed() != null && entity.getReviewed() == 1 // boolean conversion
                ))
                .collect(Collectors.toList());

    }

    @Override
    public OrderDetailDTO save(OrderDetailDTO dto) {
        OrderDetailEntity entity = new OrderDetailEntity();

        // Nếu cập nhật (id đã tồn tại)
        if (dto.getId() != null) {
            entity.setId(dto.getId());
        }

        entity.setOrderId(dto.getOrderId());
        entity.setProductId(dto.getProductId());
        entity.setQuantity(dto.getQuantity());
        entity.setReviewed(dto.getReviewed() != null && dto.getReviewed() ? 1 : 0); // Boolean → int

        // Lưu
        OrderDetailEntity saved = repository.save(entity);

        // Trả lại DTO (nên map lại từ entity đã lưu để có id mới)
        return new OrderDetailDTO(
                saved.getId(),
                saved.getOrderId(),
                saved.getProductId(),
                saved.getProduct() != null ? saved.getProduct().getName() : null,
                saved.getProduct() != null ? saved.getProduct().getOriginalPrice() : null,
                saved.getProduct() != null ? saved.getProduct().getDiscountPrice() : null,
                saved.getProduct() != null ? saved.getProduct().getDiscountPercent() : null,
                saved.getQuantity(),
                saved.getReviewed() != null && saved.getReviewed() == 1
        );
    }


    @Override
    public void deleteById(OrderDetailId id) {
        repository.deleteById(id);
    }

    @Override
    public void deleteByOrderId(Integer orderId) {
        repository.deleteByOrderId(orderId);
    }
}
