package com.handicrafts.repository;

import com.handicrafts.entity.OrderEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class OrderRepository {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Lưu đơn hàng mới hoặc cập nhật
     */
    @Transactional
    public OrderEntity save(OrderEntity entity) {
        if (entity.getId() == null) {
            entityManager.persist(entity);
            return entity;
        } else {
            return entityManager.merge(entity);
        }
    }

    /**
     * Lấy đơn hàng theo ID
     */
    public Optional<OrderEntity> findById(Integer id) {
        try {
            OrderEntity order = entityManager.find(OrderEntity.class, id);
            return Optional.ofNullable(order);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    /**
     * Lấy tất cả đơn hàng
     */
    public List<OrderEntity> findAll() {
        return entityManager.createQuery("SELECT o FROM OrderEntity o", OrderEntity.class)
                .getResultList();
    }

    /**
     * Lấy danh sách đơn hàng theo userId
     */
    public List<OrderEntity> findOrderByUserId(Integer userId) {
        TypedQuery<OrderEntity> query = entityManager.createQuery(
                "SELECT o FROM OrderEntity o WHERE o.userId = :userId ORDER BY o.createdDate DESC",
                OrderEntity.class);
        query.setParameter("userId", userId);
        return query.getResultList();
    }

    /**
     * Xoá đơn hàng theo ID
     */
    @Transactional
    public void deleteById(Integer id) {
        OrderEntity entity = entityManager.find(OrderEntity.class, id);
        if (entity != null) {
            entityManager.remove(entity);
        }
    }

    /**
     * Cập nhật đơn hàng (chủ yếu dùng khi checkout)
     */
    @Transactional
    public int updateOrder(OrderEntity order) {
        String jpql = "UPDATE OrderEntity o SET o.total = :total, o.status = :status, " +
                "o.shipToDate = :shipToDate, o.paymentMethod = :paymentMethod, " +
                "o.modifiedBy = :modifiedBy, o.modifiedDate = :modifiedDate " +
                "WHERE o.id = :id";

        return entityManager.createQuery(jpql)
                .setParameter("total", order.getTotal())
                .setParameter("status", order.getStatus())
                .setParameter("shipToDate", order.getShipToDate())
                .setParameter("paymentMethod", order.getPaymentMethod())
                .setParameter("modifiedBy", order.getModifiedBy())
                .setParameter("modifiedDate", order.getModifiedDate())
                .setParameter("id", order.getId())
                .executeUpdate();
    }

}
