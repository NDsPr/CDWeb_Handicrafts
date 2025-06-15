package com.handicrafts.entity;

import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@Table(name = "images")
public class ProductImageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 250)
    private String name;

    @Column(nullable = false, length = 5000)
    private String link;

    @Column(name = "nameInStorage", nullable = false, length = 100)
    private String nameInStorage;

    @Column(name = "createdDate")
    private Timestamp createdDate;

    @Column(name = "createdBy", length = 50)
    private String createdBy;

    @Column(name = "modifiedDate")
    private Timestamp modifiedDate;

    @Column(name = "modifiedBy", length = 50)
    private String modifiedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "productId", nullable = false)
    private ProductEntity product;

    // Constructors
    public ProductImageEntity() {}

    // Getters & Setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getNameInStorage() {
        return nameInStorage;
    }

    public void setNameInStorage(String nameInStorage) {
        this.nameInStorage = nameInStorage;
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

    public ProductEntity getProduct() {
        return product;
    }

    public void setProduct(ProductEntity product) {
        this.product = product;
    }
}

