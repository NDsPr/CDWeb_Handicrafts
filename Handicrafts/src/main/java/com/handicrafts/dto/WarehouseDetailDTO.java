package com.handicrafts.dto;

public class WarehouseDetailDTO {
    private int id;
    private int warehouseId;
    private int productId;
    private int quantity;

    public WarehouseDetailDTO() {
    }

    public WarehouseDetailDTO(int id, int warehouseId, int productId, int quantity) {
        this.id = id;
        this.warehouseId = warehouseId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(int warehouseId) {
        this.warehouseId = warehouseId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
