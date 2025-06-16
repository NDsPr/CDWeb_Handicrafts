package com.handicrafts.repository;

import com.handicrafts.dto.OrderDetailDTO;
import com.handicrafts.entity.OrderDetailEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class OrderDetailRepository {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Lấy danh sách chi tiết đơn hàng theo orderId
     */
    public List<OrderDetailEntity> findByOrderId(Integer orderId) {
        TypedQuery<OrderDetailEntity> query = entityManager.createQuery(
                "SELECT od FROM OrderDetailEntity od WHERE od.orderId = :orderId",
                OrderDetailEntity.class
        );
        query.setParameter("orderId", orderId);
        return query.getResultList();
    }

    /**
     * Tìm một chi tiết đơn hàng theo id
     */
    public Optional<OrderDetailEntity> findById(Integer id) {
        return Optional.ofNullable(entityManager.find(OrderDetailEntity.class, id));
    }

    /**
     * Thêm mới hoặc cập nhật chi tiết đơn hàng
     */
    @Transactional
    public OrderDetailEntity save(OrderDetailEntity entity) {
        if (entity.getId() == null) {
            entityManager.persist(entity);
            return entity;
        } else {
            return entityManager.merge(entity);
        }
    }

    /**
     * Xoá một chi tiết đơn hàng theo id
     */
    @Transactional
    public void deleteById(Integer id) {
        OrderDetailEntity entity = entityManager.find(OrderDetailEntity.class, id);
        if (entity != null) {
            entityManager.remove(entity);
        }
    }

    /**
     * Xoá toàn bộ chi tiết theo orderId
     */
    @Transactional
    public int deleteByOrderId(Integer orderId) {
        return entityManager.createQuery(
                        "DELETE FROM OrderDetailEntity od WHERE od.orderId = :orderId"
                )
                .setParameter("orderId", orderId)
                .executeUpdate();
    }
    public List<OrderDetailDTO> findOrderDetailByOrderId(Integer orderId) {
        String jpql = "SELECT od FROM OrderDetailEntity od WHERE od.orderId = :orderId";
        List<OrderDetailEntity> entities = entityManager.createQuery(jpql, OrderDetailEntity.class)
                .setParameter("orderId", orderId)
                .getResultList();

        return entities.stream()
                .map(entity -> {
                    var product = entity.getProduct();
                    return new OrderDetailDTO(
                            entity.getId(),
                            entity.getOrderId(),
                            entity.getProductId(),
                            product != null ? product.getName() : null,
                            product != null ? product.getOriginalPrice() : null,
                            product != null ? product.getDiscountPrice() : null,
                            product != null ? product.getDiscountPercent() : null,
                            entity.getQuantity(),
                            entity.getReviewed() != null && entity.getReviewed() == 1
                    );
                })
                .toList();
    }

}
