package com.handicrafts.repository;

import com.handicrafts.dto.OrderDetailDTO;
import com.handicrafts.entity.OrderDetailEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Repository
public class OrderDetailRepository {

    @PersistenceContext
    private EntityManager entityManager;

//    public List<OrderDetailDTO> findOrderDetailByOrderId(int orderId) {
//        List<OrderDetailDTO> result = new ArrayList<>();
//
//        String jpql = "SELECT new com.handicrafts.dto.OrderDetailDTO(od.orderId, od.productId, p.name, " +
//                "p.originalPrice, p.discountPrice, od.quantity) " +
//                "FROM OrderDetailEntity od " +
//                "INNER JOIN ProductEntity p ON od.productId = p.id " +
//                "WHERE od.orderId = :orderId";
//
//        TypedQuery<OrderDetailDTO> query = entityManager.createQuery(jpql, OrderDetailDTO.class);
//        query.setParameter("orderId", orderId);
//
//        try {
//            result = query.getResultList();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return result;
//    }

//    @Transactional
//    public int createOrderDetail(OrderDetailDTO orderDetailDTO) {
//        int affectedRows = 0;
//
//        OrderDetailEntity entity = new OrderDetailEntity();
//        entity.setOrderId(orderDetailDTO.getOrderId());
//        entity.setProductId(orderDetailDTO.getProductId());
//        entity.setQuantity(orderDetailDTO.getQuantity());
//
//        try {
//            entityManager.persist(entity);
//            affectedRows = 1;
//        } catch (Exception e) {
//            e.printStackTrace();
//            affectedRows = 0;
//        }
//
//        return affectedRows;
//    }

//    @Transactional
//    public int createOrderDetails(List<OrderDetailDTO> orderDetails) {
//        int affectedRows = 0;
//
//        try {
//            for (OrderDetailDTO dto : orderDetails) {
//                OrderDetailEntity entity = new OrderDetailEntity();
//                entity.setOrderId(dto.getOrderId());
//                entity.setProductId(dto.getProductId());
//                entity.setQuantity(dto.getQuantity());
//
//                entityManager.persist(entity);
//                affectedRows++;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            return 0;
//        }
//
//        return affectedRows;
//    }

    @Transactional
    public int deleteOrderDetailsByOrderId(int orderId) {
        int affectedRows = 0;

        String jpql = "DELETE FROM OrderDetailEntity od WHERE od.id = :orderId";

        Query query = entityManager.createQuery(jpql);
        query.setParameter("orderId", orderId);

        try {
            affectedRows = query.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return affectedRows;
    }

//    public List<OrderDetailDTO> findOrderDetailsByOrderIds(List<Integer> orderIds) {
//        List<OrderDetailDTO> result = new ArrayList<>();
//
//        if (orderIds == null || orderIds.isEmpty()) {
//            return result;
//        }
//
//        String jpql = "SELECT new com.handicrafts.dto.OrderDetailDTO(od.orderId, od.productId, p.name, " +
//                "p.originalPrice, p.discountPrice, od.quantity) " +
//                "FROM OrderDetailEntity od " +
//                "INNER JOIN ProductEntity p ON od.productId = p.id " +
//                "WHERE od.orderId IN :orderIds";
//
//        TypedQuery<OrderDetailDTO> query = entityManager.createQuery(jpql, OrderDetailDTO.class);
//        query.setParameter("orderIds", orderIds);
//
//        try {
//            result = query.getResultList();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return result;
//    }

    public int countOrderDetailsByProductId(int productId) {
        int count = 0;

        String jpql = "SELECT COUNT(od) FROM OrderDetailEntity od WHERE od.id = :productId";

        TypedQuery<Long> query = entityManager.createQuery(jpql, Long.class);
        query.setParameter("productId", productId);

        try {
            Long result = query.getSingleResult();
            count = result.intValue();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return count;
    }

//    @Transactional
//    public int updateOrderDetail(OrderDetailDTO orderDetailDTO) {
//        int affectedRows = 0;
//
//        String jpql = "UPDATE OrderDetailEntity od SET od.quantity = :quantity " +
//                "WHERE od.orderId = :orderId AND od.productId = :productId";
//
//        Query query = entityManager.createQuery(jpql);
//        query.setParameter("quantity", orderDetailDTO.getQuantity());
//        query.setParameter("orderId", orderDetailDTO.getOrderId());
//        query.setParameter("productId", orderDetailDTO.getProductId());
//
//        try {
//            affectedRows = query.executeUpdate();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return affectedRows;
//    }

//    public OrderDetailDTO findOrderDetailByOrderIdAndProductId(int orderId, int productId) {
//        OrderDetailDTO result = null;
//
//        String jpql = "SELECT new com.handicrafts.dto.OrderDetailDTO(od.orderId, od.productId, p.name, " +
//                "p.originalPrice, p.discountPrice, od.quantity) " +
//                "FROM OrderDetailEntity od " +
//                "INNER JOIN ProductEntity p ON od.productId = p.id " +
//                "WHERE od.orderId = :orderId AND od.productId = :productId";
//
//        TypedQuery<OrderDetailDTO> query = entityManager.createQuery(jpql, OrderDetailDTO.class);
//        query.setParameter("orderId", orderId);
//        query.setParameter("productId", productId);
//
//        try {
//            result = query.getSingleResult();
//        } catch (Exception e) {
//            // No result found or other exception
//        }
//
//        return result;
//    }
}
