package com.handicrafts.repository;

import com.handicrafts.dto.OrderDetailDTO;
import com.handicrafts.entity.OrderDetailEntity;
import com.handicrafts.entity.OrderDetailId;
import com.handicrafts.entity.OrderEntity;
import com.handicrafts.entity.ProductEntity;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;

import java.util.List;
import java.util.Optional;

@Repository
public class OrderDetailRepository {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Find all order details for a specific order ID
     * @param orderId The order ID as a String
     * @return List of OrderDetailDTO objects
     */
    public List<OrderDetailDTO> findDetailByOrderId(String orderId) {
        int orderIdConvert = Integer.parseInt(orderId);

        String jpql = "SELECT new com.handicrafts.dto.OrderDetailDTO(od.id.orderId, od.id.productId, p.name, " +
                "p.originalPrice, p.discountPrice, p.discountPercent, od.quantity, " +
                "CASE WHEN od.reviewed = true THEN 1 ELSE 0 END) " +
                "FROM OrderDetailEntity od JOIN od.product p " +
                "WHERE od.id.orderId = :orderId";

        TypedQuery<OrderDetailDTO> query = entityManager.createQuery(jpql, OrderDetailDTO.class);
        query.setParameter("orderId", orderIdConvert);

        return query.getResultList();
    }

    /**
     * Create a new order detail record
     * @param orderDetailBean The DTO containing order detail information
     * @return Number of rows affected (should be 1 for success)
     */
    @Transactional
    public int createOrderDetail(OrderDetailDTO orderDetailBean) {
        String sql = "INSERT INTO order_details (orderId, productId, quantity, reviewed) " +
                "VALUES (:orderId, :productId, :quantity, 0)";

        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("orderId", orderDetailBean.getOrderId());
        query.setParameter("productId", orderDetailBean.getProductId());
        query.setParameter("quantity", orderDetailBean.getQuantity());

        return query.executeUpdate();
    }

    /**
     * Create a new order detail using JPA entity
     * @param orderDetail The OrderDetailEntity to persist
     */
    @Transactional
    public void save(OrderDetailEntity orderDetail) {
        entityManager.persist(orderDetail);
    }

    /**
     * Find an order detail by its composite key
     * @param orderId The order ID
     * @param productId The product ID
     * @return Optional containing the entity if found
     */
    public Optional<OrderDetailEntity> findById(int orderId, int productId) {
        OrderDetailId id = new OrderDetailId(orderId, productId);
        OrderDetailEntity orderDetail = entityManager.find(OrderDetailEntity.class, id);
        return Optional.ofNullable(orderDetail);
    }

    /**
     * Update the reviewed status of an order detail
     * @param orderId The order ID
     * @param productId The product ID
     * @param reviewed The new reviewed status
     * @return Number of rows affected (should be 1 for success)
     */
    @Transactional
    public int updateReviewStatus(int orderId, int productId, boolean reviewed) {
        String jpql = "UPDATE OrderDetailEntity od SET od.reviewed = :reviewed " +
                "WHERE od.id.orderId = :orderId AND od.id.productId = :productId";

        Query query = entityManager.createQuery(jpql);
        query.setParameter("reviewed", reviewed);
        query.setParameter("orderId", orderId);
        query.setParameter("productId", productId);

        return query.executeUpdate();
    }

    /**
     * Delete an order detail by its composite key
     * @param orderId The order ID
     * @param productId The product ID
     * @return Number of rows affected (should be 1 for success)
     */
    @Transactional
    public int deleteOrderDetail(int orderId, int productId) {
        String jpql = "DELETE FROM OrderDetailEntity od " +
                "WHERE od.id.orderId = :orderId AND od.id.productId = :productId";

        Query query = entityManager.createQuery(jpql);
        query.setParameter("orderId", orderId);
        query.setParameter("productId", productId);

        return query.executeUpdate();
    }

    /**
     * Find all order details for multiple order IDs
     * @param orderIds List of order IDs
     * @return List of OrderDetailDTO objects
     */
    public List<OrderDetailDTO> findDetailsByOrderIds(List<Integer> orderIds) {
        String jpql = "SELECT new com.handicrafts.dto.OrderDetailDTO(od.id.orderId, od.id.productId, p.name, " +
                "p.originalPrice, p.discountPrice, p.discountPercent, od.quantity, " +
                "CASE WHEN od.reviewed = true THEN 1 ELSE 0 END) " +
                "FROM OrderDetailEntity od JOIN od.product p " +
                "WHERE od.id.orderId IN :orderIds";

        TypedQuery<OrderDetailDTO> query = entityManager.createQuery(jpql, OrderDetailDTO.class);
        query.setParameter("orderIds", orderIds);

        return query.getResultList();
    }

    /**
     * Count the number of order details for a specific order
     * @param orderId The order ID
     * @return The count of order details
     */
    public long countByOrderId(int orderId) {
        String jpql = "SELECT COUNT(od) FROM OrderDetailEntity od WHERE od.id.orderId = :orderId";

        TypedQuery<Long> query = entityManager.createQuery(jpql, Long.class);
        query.setParameter("orderId", orderId);

        return query.getSingleResult();
    }

    /**
     * Update the quantity of an order detail
     * @param orderId The order ID
     * @param productId The product ID
     * @param quantity The new quantity
     * @return Number of rows affected (should be 1 for success)
     */
    @Transactional
    public int updateQuantity(int orderId, int productId, int quantity) {
        String jpql = "UPDATE OrderDetailEntity od SET od.quantity = :quantity " +
                "WHERE od.id.orderId = :orderId AND od.id.productId = :productId";

        Query query = entityManager.createQuery(jpql);
        query.setParameter("quantity", quantity);
        query.setParameter("orderId", orderId);
        query.setParameter("productId", productId);

        return query.executeUpdate();
    }

    /**
     * Delete all order details for a specific order
     * @param orderId The order ID
     * @return Number of rows affected
     */
    @Transactional
    public int deleteByOrderId(int orderId) {
        String jpql = "DELETE FROM OrderDetailEntity od WHERE od.id.orderId = :orderId";

        Query query = entityManager.createQuery(jpql);
        query.setParameter("orderId", orderId);

        return query.executeUpdate();
    }

    /**
     * Find all products purchased by a specific customer
     * @param customerId The customer ID
     * @return List of product IDs
     */
    public List<Integer> findProductIdsByCustomerId(int customerId) {
        String jpql = "SELECT DISTINCT od.id.productId FROM OrderDetailEntity od " +
                "JOIN od.order o WHERE o.customer.id = :customerId";

        TypedQuery<Integer> query = entityManager.createQuery(jpql, Integer.class);
        query.setParameter("customerId", customerId);

        return query.getResultList();
    }

    /**
     * Create multiple order details in a batch
     * @param orderDetails List of OrderDetailDTO objects
     * @return Number of rows affected
     */
    @Transactional
    public int batchCreateOrderDetails(List<OrderDetailDTO> orderDetails) {
        int rowsAffected = 0;

        for (OrderDetailDTO detail : orderDetails) {
            String sql = "INSERT INTO order_details (orderId, productId, quantity, reviewed) " +
                    "VALUES (:orderId, :productId, :quantity, 0)";

            Query query = entityManager.createNativeQuery(sql);
            query.setParameter("orderId", detail.getOrderId());
            query.setParameter("productId", detail.getProductId());
            query.setParameter("quantity", detail.getQuantity());

            rowsAffected += query.executeUpdate();
        }

        return rowsAffected;
    }
}
