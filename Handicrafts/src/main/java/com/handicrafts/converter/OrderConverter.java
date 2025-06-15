package com.handicrafts.converter;

import com.handicrafts.dto.OrderDTO;
import com.handicrafts.dto.UserDTO;
import com.handicrafts.entity.OrderEntity;
import com.handicrafts.entity.UserEntity;
import com.handicrafts.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.Optional;

@Component
public class OrderConverter {

    @Autowired
    private UserRepository userRepository;

    /**
     * Chuyển đổi từ OrderEntity sang OrderDTO
     */
    public OrderDTO toDTO(OrderEntity entity) {
        if (entity == null) {
            return null;
        }

        OrderDTO dto = new OrderDTO();
        dto.setId(entity.getId());
        dto.setId(entity.getUser() != null ? entity.getUser().getUserID() : null);
        dto.setStatus(entity.getStatus());
        dto.setTotal(entity.getTotal());
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setModifiedDate(entity.getModifiedDate());

        // Thêm các thông tin khác nếu cần
        if (entity.getUser() != null) {
            dto.setUserName(entity.getUser().getUsername());
        }

        return dto;
    }

    /**
     * Chuyển đổi từ OrderDTO sang OrderEntity
     */
    public OrderEntity toEntity(OrderDTO dto) {
        if (dto == null) {
            return null;
        }

        OrderEntity entity = new OrderEntity();

        // Nếu đang cập nhật một entity đã tồn tại
        if (dto.getId() != 0) {
            entity.setId(dto.getId());
        }

        // Thiết lập thông tin người dùng
        if (dto.getId() != null) {
            Optional<UserEntity> userOptional = userRepository.findUserById(dto.getId());
            userOptional.ifPresent(entity::setUser);
        }

        entity.setStatus(dto.getStatus());
        entity.setTotal(dto.getTotal());

        // Xử lý thời gian
        if (dto.getCreatedDate() != null) {
            entity.setCreatedDate(dto.getCreatedDate());
        } else {
            entity.setCreatedDate(new Timestamp(System.currentTimeMillis()));
        }

        if (dto.getModifiedDate() != null) {
            entity.setModifiedDate(dto.getModifiedDate());
        } else {
            entity.setModifiedDate(new Timestamp(System.currentTimeMillis()));
        }

        return entity;
    }

    /**
     * Cập nhật entity từ DTO (sử dụng cho update)
     */
    public OrderEntity updateEntityFromDTO(OrderDTO dto, OrderEntity entity) {
        if (dto == null || entity == null) {
            return entity;
        }

        // Chỉ cập nhật các trường cần thiết, giữ nguyên ID và các trường không thay đổi
        if (dto.getId() != null && (entity.getUser() == null || !dto.getId().equals(entity.getUser().getId()))) {
            UserDTO userOptional = userRepository.findUserById(dto.getUserId());
            userOptional.ifPresent(entity::setUser);
        }
         if (dto.getStatus() != null) entity.setStatus(dto.getStatus());
        if (dto.getTotal() != null) entity.setTotal(dto.getTotal());

        // Luôn cập nhật thời gian cập nhật
        entity.setModifiedDate(new Timestamp(System.currentTimeMillis()));

        return entity;
    }
}
