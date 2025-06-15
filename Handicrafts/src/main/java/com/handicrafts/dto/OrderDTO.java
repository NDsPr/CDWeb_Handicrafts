package com.handicrafts.dto;

import com.handicrafts.entity.OrderEntity;

import java.sql.Timestamp;

public class OrderDTO {

    private Integer id;
    private Integer userId;
    private Timestamp createdDate;
    private Timestamp shipToDate;
    private Double total;
    private String paymentMethod;
    private Integer status;
    private String createdBy;
    private Timestamp modifiedDate;
    private String modifiedBy;

    // Thêm các trường bổ sung cho DTO (không có trong Entity)
    private String userName; // Tên người dùng đặt hàng
    private String statusName; // Tên trạng thái đơn hàng (đã xử lý, đang giao, v.v.)

    // Constructors
    public OrderDTO() {
    }

    // Constructor để chuyển đổi từ Entity sang DTO
    public OrderDTO(OrderEntity entity) {
        this.id = entity.getId();
        this.userId = entity.getUserId();
        this.createdDate = entity.getCreatedDate();
        this.shipToDate = entity.getShipToDate();
        this.total = entity.getTotal();
        this.paymentMethod = entity.getPaymentMethod();
        this.status = entity.getStatus();
        this.createdBy = entity.getCreatedBy();
        this.modifiedDate = entity.getModifiedDate();
        this.modifiedBy = entity.getModifiedBy();

        // Nếu entity có thông tin user, lấy tên user
        if (entity.getUser() != null) {
            this.userName = entity.getUser().getUsername(); // Giả sử UserEntity có trường username
        }

        // Chuyển đổi status code sang tên trạng thái
        this.statusName = convertStatusToString(entity.getStatus());
    }

    // Phương thức chuyển đổi từ DTO sang Entity
    public OrderEntity toEntity() {
        OrderEntity entity = new OrderEntity();
        entity.setId(this.id);
        entity.setUserId(this.userId);
        entity.setCreatedDate(this.createdDate);
        entity.setShipToDate(this.shipToDate);
        entity.setTotal(this.total);
        entity.setPaymentMethod(this.paymentMethod);
        entity.setStatus(this.status);
        entity.setCreatedBy(this.createdBy);
        entity.setModifiedDate(this.modifiedDate);
        entity.setModifiedBy(this.modifiedBy);
        return entity;
    }

    // Helper method để chuyển đổi status code sang tên trạng thái
    private String convertStatusToString(Integer status) {
        if (status == null) return "";

        switch (status) {
            case 0: return "Chờ xác nhận";
            case 1: return "Đã xác nhận";
            case 2: return "Đang giao hàng";
            case 3: return "Đã giao hàng";
            case 4: return "Đã hủy";
            default: return "Không xác định";
        }
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    public Timestamp getShipToDate() {
        return shipToDate;
    }

    public void setShipToDate(Timestamp shipToDate) {
        this.shipToDate = shipToDate;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
        this.statusName = convertStatusToString(status);
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Timestamp getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Timestamp modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }
}
