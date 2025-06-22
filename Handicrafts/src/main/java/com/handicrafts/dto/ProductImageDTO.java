package com.handicrafts.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class ProductImageDTO {
    private int id;
    private String name;
    private String nameInStorage;
    private String link;
    private int productId;
    private Timestamp createdDate;
    private String createdBy;
    private Timestamp modifiedDate;
    private String modifiedBy;

    public ProductImageDTO() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameInStorage() {
        return nameInStorage;
    }

    public void setNameInStorage(String nameInStorage) {
        this.nameInStorage = nameInStorage;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
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
}