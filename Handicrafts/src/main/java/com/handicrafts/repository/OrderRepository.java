package com.handicrafts.repository;

import com.handicrafts.dto.OrderDTO;
import com.handicrafts.entity.OrderEntity;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
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
    public OrderDTO findOrderById(int id) {
        try {
            // Sử dụng JPQL để truy vấn đơn hàng theo ID
            String jpql = "SELECT o FROM OrderEntity o WHERE o.id = :id";
            OrderDTO order = entityManager.createQuery(jpql, OrderDTO.class)
                    .setParameter("id", id)
                    .getSingleResult();

            // Chuyển đổi Order thành OrderDTO
            OrderDTO orderDTO = new OrderDTO();
            orderDTO.setId(order.getId());
            orderDTO.setStatus(order.getStatus());
            // Thiết lập các thuộc tính khác của OrderDTO từ order
            // orderDTO.setCustomerId(order.getCustomer().getId());
            // orderDTO.setOrderDate(order.getOrderDate());
            // orderDTO.setTotalAmount(order.getTotalAmount());
            // ...

            return orderDTO;
        } catch (NoResultException e) {
            return null; // Trả về null nếu không tìm thấy đơn hàng
        }
    }

    /**
     * Hủy đơn hàng với quyền admin
     * @param id ID của đơn hàng cần hủy
     * @return số bản ghi bị ảnh hưởng
     */
    @Transactional
    public int cancelOrderAdmin(int id) {
        // Sử dụng JPQL để cập nhật trạng thái đơn hàng
        String jpql = "UPDATE OrderEntity o SET o.status = 'CANCELLED', o.createdDate = CURRENT_TIMESTAMP, " +
                "o.createdBy = 'ADMIN' WHERE o.id = :id";

        return entityManager.createQuery(jpql)
                .setParameter("id", id)
                .executeUpdate();
    }
    public List<OrderDTO> getOrderDatatable(int start, int length, String columnOrder, String orderDir, String searchValue) {
        // Xác định tên cột để sắp xếp
        String orderColumn = mapColumnName(columnOrder);

        // Xây dựng câu truy vấn JPQL cơ bản
        StringBuilder jpqlBuilder = new StringBuilder("SELECT o FROM YourOrderEntity o");

        // Thêm điều kiện tìm kiếm nếu có
        if (searchValue != null && !searchValue.isEmpty()) {
            jpqlBuilder.append(" WHERE LOWER(o.id) LIKE LOWER(:searchValue) OR ")
                    .append("LOWER(o.customerName) LIKE LOWER(:searchValue) OR ")
                    .append("LOWER(o.status) LIKE LOWER(:searchValue)");
            // Thêm các điều kiện tìm kiếm khác nếu cần
        }

        // Thêm sắp xếp
        jpqlBuilder.append(" ORDER BY o.").append(orderColumn).append(" ").append(orderDir);

        // Tạo truy vấn
        TypedQuery<Object> query = entityManager.createQuery(jpqlBuilder.toString(), Object.class);

        // Thiết lập tham số tìm kiếm nếu có
        if (searchValue != null && !searchValue.isEmpty()) {
            query.setParameter("searchValue", "%" + searchValue + "%");
        }

        // Thiết lập phân trang
        query.setFirstResult(start);
        query.setMaxResults(length);

        // Thực thi truy vấn
        List<Object> results = query.getResultList();

        // Chuyển đổi kết quả sang OrderDTO
        List<OrderDTO> orderDTOs = new ArrayList<>();
        for (Object result : results) {
            OrderDTO dto = convertToOrderDTO(result);
            orderDTOs.add(dto);
        }

        return orderDTOs;
    }

    /**
     * Lấy tổng số bản ghi đơn hàng
     * @return Tổng số bản ghi
     */
    public int getRecordsTotal() {
        Query query = entityManager.createQuery("SELECT COUNT(o) FROM OrderEntity o");
        return ((Long) query.getSingleResult()).intValue();
    }

    /**
     * Lấy tổng số bản ghi đơn hàng sau khi áp dụng bộ lọc tìm kiếm
     * @param searchValue Giá trị tìm kiếm
     * @return Tổng số bản ghi sau khi lọc
     */
    public int getRecordsFiltered(String searchValue) {
        // Nếu không có giá trị tìm kiếm, trả về tổng số bản ghi
        if (searchValue == null || searchValue.isEmpty()) {
            return getRecordsTotal();
        }

        // Xây dựng câu truy vấn đếm với điều kiện tìm kiếm
        StringBuilder jpqlBuilder = new StringBuilder("SELECT COUNT(o) FROM YourOrderEntity o");
        jpqlBuilder.append(" WHERE LOWER(o.id) LIKE LOWER(:searchValue) OR ")
                .append("LOWER(o.customerName) LIKE LOWER(:searchValue) OR ")
                .append("LOWER(o.status) LIKE LOWER(:searchValue)");
        // Thêm các điều kiện tìm kiếm khác nếu cần

        // Tạo và thực thi truy vấn
        Query query = entityManager.createQuery(jpqlBuilder.toString());
        query.setParameter("searchValue", "%" + searchValue + "%");

        return ((Long) query.getSingleResult()).intValue();
    }

    /**
     * Ánh xạ tên cột từ datatable sang tên cột trong entity
     * @param columnOrder Tên cột từ datatable
     * @return Tên cột trong entity
     */
    private String mapColumnName(String columnOrder) {
        // Ánh xạ tên cột từ datatable sang tên cột trong entity
        switch (columnOrder) {
            case "0": return "id";
            case "1": return "orderDate";
            case "2": return "customerName";
            case "3": return "totalAmount";
            case "4": return "status";
            // Thêm các trường hợp khác nếu cần
            default: return "id"; // Mặc định sắp xếp theo id
        }
    }

    /**
     * Chuyển đổi từ entity sang OrderDTO
     * @param entity Entity đơn hàng
     * @return OrderDTO
     */
    private OrderDTO convertToOrderDTO(Object entity) {
        // Thay YourOrderEntity bằng tên thực tế của entity đơn hàng
        // YourOrderEntity order = (YourOrderEntity) entity;

        OrderDTO dto = new OrderDTO();
        // Thiết lập các thuộc tính của dto từ entity
        // dto.setId(order.getId());
        // dto.setCustomerName(order.getCustomerName());
        // dto.setOrderDate(order.getOrderDate());
        // dto.setStatus(order.getStatus());
        // dto.setTotalAmount(order.getTotalAmount());
        // Thiết lập các thuộc tính khác nếu cần

        return dto;
    }
    public Integer countTotalOrders() {
        TypedQuery<Long> query = entityManager.createQuery(
                "SELECT COUNT(o) FROM OrderEntity o", Long.class);
        return query.getSingleResult().intValue();
    }

    public Integer countCompletedOrders() {
        TypedQuery<Long> query = entityManager.createQuery(
                "SELECT COUNT(o) FROM OrderEntity o WHERE o.status = 1", Long.class);
        // Điều chỉnh điều kiện theo cấu trúc thực tế của bạn
        return query.getSingleResult().intValue();
    }

    public List<Integer> countOrder() {
        List<Integer> result = new ArrayList<>();
        result.add(countTotalOrders());
        result.add(countCompletedOrders());
        return result;
    }

}
