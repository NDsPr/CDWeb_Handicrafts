package com.handicrafts.dto;


import java.time.LocalDate;

public class Category_typeDTO {
    private int category_typeID;
    private String name;
    private String category_typeCode;
    private LocalDate createdAt;
    private LocalDate updatedAt;

    public int getCategory_typeID() {
        return category_typeID;
    }

    public void setCategory_typeID(int authorID) {
        this.category_typeID = category_typeID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory_typeCode() {
        return category_typeCode;
    }

    public void setCategory_typeCode(String category_typeCode) {
        this.category_typeCode = category_typeCode;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDate getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDate updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "Category_typeDTO{" +
                "Category_typeID=" + category_typeID +
                ", name='" + name + '\'' +
                ", category_typeCode='" + category_typeCode + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
