package com.handicrafts.repository;

import com.handicrafts.entity.OrderDetailEntity;
import com.handicrafts.entity.OrderDetailId;
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

    public Optional<OrderDetailEntity> findById(OrderDetailId id) {
        OrderDetailEntity detail = entityManager.find(OrderDetailEntity.class, id);
        return Optional.ofNullable(detail);
    }

    public List<OrderDetailEntity> findByOrderId(Integer orderId) {
        TypedQuery<OrderDetailEntity> query = entityManager.createQuery(
                "SELECT od FROM OrderDetailEntity od WHERE od.id.orderId = :orderId", OrderDetailEntity.class);
        query.setParameter("orderId", orderId);
        return query.getResultList();
    }

    @Transactional
    public OrderDetailEntity save(OrderDetailEntity entity) {
        if (findById(entity.getId()).isPresent()) {
            return entityManager.merge(entity);
        } else {
            entityManager.persist(entity);
            return entity;
        }
    }

    @Transactional
    public void deleteById(OrderDetailId id) {
        findById(id).ifPresent(entityManager::remove);
    }

    @Transactional
    public void deleteByOrderId(Integer orderId) {
        entityManager.createQuery("DELETE FROM OrderDetailEntity od WHERE od.id.orderId = :orderId")
                .setParameter("orderId", orderId)
                .executeUpdate();
    }
}
