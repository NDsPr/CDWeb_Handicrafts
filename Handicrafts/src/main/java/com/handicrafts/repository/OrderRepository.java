package com.handicrafts.repository;

import com.handicrafts.dto.OrderDTO;
import com.handicrafts.entity.OrderEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Repository
public class OrderRepository {

    @PersistenceContext
    private EntityManager entityManager;
//
//    public List<OrderDTO> findOrderByUserId(int userId) {
//        List<OrderDTO> orderList = new ArrayList<>();
//
//        String jpql = "SELECT new com.handicrafts.dto.OrderDTO(o.id, o.userId, o.total, o.paymentMethod, " +
//                "o.status, o.shipToDate, o.createdDate, o.createdBy, o.modifiedDate, o.modifiedBy) " +
//                "FROM OrderEntity o WHERE o.userId = :userId";
//
//        TypedQuery<OrderDTO> query = entityManager.createQuery(jpql, OrderDTO.class);
//        query.setParameter("userId", userId);
//
//        try {
//            orderList = query.getResultList();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return orderList;
//    }

//    public List<OrderDTO> findAllOrders() {
//        List<OrderDTO> orderList = new ArrayList<>();
//
//        String jpql = "SELECT new com.handicrafts.dto.OrderDTO(o.id, o.userId, o.total, o.paymentMethod, " +
//                "o.status, o.shipToDate, o.createdDate, o.createdBy, o.modifiedDate, o.modifiedBy) " +
//                "FROM OrderEntity o";
//
//        TypedQuery<OrderDTO> query = entityManager.createQuery(jpql, OrderDTO.class);
//
//        try {
//            orderList = query.getResultList();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return orderList;
//    }

//    public OrderDTO findOrderById(int id) {
//        OrderDTO order = null;
//
//        String jpql = "SELECT new com.handicrafts.dto.OrderDTO(o.id, o.userId, o.total, o.paymentMethod, " +
//                "o.status, o.shipToDate, o.createdDate, o.createdBy, o.modifiedDate, o.modifiedBy) " +
//                "FROM OrderEntity o WHERE o.id = :id";
//
//        TypedQuery<OrderDTO> query = entityManager.createQuery(jpql, OrderDTO.class);
//        query.setParameter("id", id);
//
//        try {
//            order = query.getSingleResult();
//        } catch (Exception e) {
//            // No result found or other exception
//        }
//
//        return order;
//    }

    @Transactional
    public int updateOrder(OrderDTO orderBean) {
        int affectedRows = 0;

        String jpql = "UPDATE OrderEntity o SET o.status = :status WHERE o.id = :id";

        Query query = entityManager.createQuery(jpql);
        query.setParameter("status", orderBean.getStatus());
        query.setParameter("id", orderBean.getId());

        try {
            affectedRows = query.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return affectedRows;
    }

    @Transactional
    public int createOrder(OrderDTO orderBean) {
        int id = -1;

        OrderEntity entity = new OrderEntity();
        entity.setUserId(orderBean.getUserId());
        entity.setCreatedDate(orderBean.getCreatedDate());
        entity.setShipToDate(orderBean.getShipToDate());
        entity.setTotal(orderBean.getTotal());
        entity.setPaymentMethod(orderBean.getPaymentMethod());
        entity.setStatus(1); // Default status is 1
        entity.setCreatedBy(orderBean.getCreatedBy());
        entity.setModifiedDate(orderBean.getModifiedDate());
        entity.setModifiedBy(orderBean.getModifiedBy());

        try {
            entityManager.persist(entity);
            entityManager.flush();
            id = entity.getId();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return id;
    }

    @Transactional
    public int cancelOrder(int orderId) {
        int affected = 0;

        String jpql = "UPDATE OrderEntity o SET o.status = 0 WHERE o.id = :orderId AND o.status NOT IN (0, 3, 4)";

        Query query = entityManager.createQuery(jpql);
        query.setParameter("orderId", orderId);

        try {
            affected = query.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return affected;
    }

    @Transactional
    public int cancelOrderAdmin(int id) {
        int affectRows = 0;

        String jpql = "UPDATE OrderEntity o SET o.status = 0 WHERE o.id = :id";

        Query query = entityManager.createQuery(jpql);
        query.setParameter("id", id);

        try {
            affectRows = query.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return affectRows;
    }

    public List<OrderDTO> getOrderDatatable(int start, int length, String columnOrder, String orderDir, String searchValue) {
        List<OrderDTO> orders = new ArrayList<>();

        StringBuilder jpql = new StringBuilder();
        jpql.append("SELECT new com.handicrafts.dto.OrderDTO(o.id, o.userId, o.total, o.paymentMethod, ");
        jpql.append("o.status, o.shipToDate, o.createdDate, o.createdBy, o.modifiedDate, o.modifiedBy) ");
        jpql.append("FROM OrderEntity o ");

        // Add search conditions if searchValue is provided
        if (searchValue != null && !searchValue.isEmpty()) {
            jpql.append("WHERE (CAST(o.id AS string) LIKE :searchPattern OR ");
            jpql.append("CAST(o.userId AS string) LIKE :searchPattern OR ");
            jpql.append("CAST(o.total AS string) LIKE :searchPattern OR ");
            jpql.append("o.paymentMethod LIKE :searchPattern OR ");
            jpql.append("CAST(o.status AS string) LIKE :searchPattern OR ");
            jpql.append("CAST(o.shipToDate AS string) LIKE :searchPattern OR ");
            jpql.append("CAST(o.createdDate AS string) LIKE :searchPattern OR ");
            jpql.append("o.createdBy LIKE :searchPattern OR ");
            jpql.append("CAST(o.modifiedDate AS string) LIKE :searchPattern OR ");
            jpql.append("o.modifiedBy LIKE :searchPattern) ");
        }

        // Add order by clause
        jpql.append("ORDER BY o.").append(columnOrder).append(" ").append(orderDir);

        TypedQuery<OrderDTO> query = entityManager.createQuery(jpql.toString(), OrderDTO.class);

        // Set search parameter if provided
        if (searchValue != null && !searchValue.isEmpty()) {
            query.setParameter("searchPattern", "%" + searchValue + "%");
        }

        // Apply pagination
        query.setFirstResult(start);
        query.setMaxResults(length);

        try {
            orders = query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return orders;
    }

    public int getRecordsTotal() {
        int recordsTotal = 0;

        String jpql = "SELECT COUNT(o.id) FROM OrderEntity o";

        TypedQuery<Long> query = entityManager.createQuery(jpql, Long.class);

        try {
            Long count = query.getSingleResult();
            recordsTotal = count.intValue();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return recordsTotal;
    }

    public int getRecordsFiltered(String searchValue) {
        int recordsFiltered = 0;

        StringBuilder jpql = new StringBuilder();
        jpql.append("SELECT COUNT(o.id) FROM OrderEntity o ");

        // Add search conditions if searchValue is provided
        if (searchValue != null && !searchValue.isEmpty()) {
            jpql.append("WHERE (CAST(o.id AS string) LIKE :searchPattern OR ");
            jpql.append("CAST(o.userId AS string) LIKE :searchPattern OR ");
            jpql.append("CAST(o.total AS string) LIKE :searchPattern OR ");
            jpql.append("o.paymentMethod LIKE :searchPattern OR ");
            jpql.append("CAST(o.status AS string) LIKE :searchPattern OR ");
            jpql.append("CAST(o.shipToDate AS string) LIKE :searchPattern OR ");
            jpql.append("CAST(o.createdDate AS string) LIKE :searchPattern OR ");
            jpql.append("o.createdBy LIKE :searchPattern OR ");
            jpql.append("CAST(o.modifiedDate AS string) LIKE :searchPattern OR ");
            jpql.append("o.modifiedBy LIKE :searchPattern) ");
        }

        TypedQuery<Long> query = entityManager.createQuery(jpql.toString(), Long.class);

        // Set search parameter if provided
        if (searchValue != null && !searchValue.isEmpty()) {
            query.setParameter("searchPattern", "%" + searchValue + "%");
        }

        try {
            Long count = query.getSingleResult();
            recordsFiltered = count.intValue();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return recordsFiltered;
    }
}
