package com.handicrafts.dto;

import java.sql.Timestamp;

public class WarehouseDTO {
    private int id;
    private String shippingFrom;
    private Timestamp shippingStart;
    private Timestamp shippingDone;
    private String description;
    private Timestamp createdDate;
    private String createdBy;

    public WarehouseDTO() {
    }

    public WarehouseDTO(int id, String shippingFrom, Timestamp shippingStart, Timestamp shippingDone, String description, Timestamp createdDate, String createdBy) {
        this.id = id;
        this.shippingFrom = shippingFrom;
        this.shippingStart = shippingStart;
        this.shippingDone = shippingDone;
        this.description = description;
        this.createdDate = createdDate;
        this.createdBy = createdBy;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getShippingFrom() {
        return shippingFrom;
    }

    public void setShippingFrom(String shippingFrom) {
        this.shippingFrom = shippingFrom;
    }

    public Timestamp getShippingStart() {
        return shippingStart;
    }

    public void setShippingStart(Timestamp shippingStart) {
        this.shippingStart = shippingStart;
    }

    public Timestamp getShippingDone() {
        return shippingDone;
    }

    public void setShippingDone(Timestamp shippingDone) {
        this.shippingDone = shippingDone;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
}
