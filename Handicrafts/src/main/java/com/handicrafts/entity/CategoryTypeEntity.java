package com.handicrafts.entity;

import jakarta.persistence.*;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "category_types")
public class CategoryTypeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(columnDefinition = "text")
    private String description;

    @Column(name = "categoryId", nullable = false)
    private Integer categoryId;

    @Column(name = "idOnBrowser", length = 50)
    private String idOnBrowser;

    @Column(nullable = false)
    private Integer status;

    @Column(name = "createdDate")
    private Timestamp createdDate;

    @Column(name = "createdBy", length = 50)
    private String createdBy;

    @Column(name = "modifiedDate")
    private Timestamp modifiedDate;

    @Column(name = "modifiedBy", length = 50)
    private String modifiedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoryId", referencedColumnName = "id", insertable = false, updatable = false)
    private CategoryEntity category;

    @OneToMany(mappedBy = "categoryType", cascade = CascadeType.ALL)
    private Set<ProductEntity> products = new HashSet<>();

    // Default constructor
    public CategoryTypeEntity() {
    }

    // Constructor with essential fields
    public CategoryTypeEntity(String name, Integer categoryId, Integer status) {
        this.name = name;
        this.categoryId = categoryId;
        this.status = status;
        this.createdDate = new Timestamp(System.currentTimeMillis());
    }

    // Full constructor
    public CategoryTypeEntity(String name, String description, Integer categoryId,
                              String idOnBrowser, Integer status, String createdBy) {
        this.name = name;
        this.description = description;
        this.categoryId = categoryId;
        this.idOnBrowser = idOnBrowser;
        this.status = status;
        this.createdBy = createdBy;
        this.createdDate = new Timestamp(System.currentTimeMillis());
    }

    // Getters and Setters
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getIdOnBrowser() {
        return idOnBrowser;
    }

    public void setIdOnBrowser(String idOnBrowser) {
        this.idOnBrowser = idOnBrowser;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

    public CategoryEntity getCategory() {
        return category;
    }

    public void setCategory(CategoryEntity category) {
        this.category = category;
    }

    public Set<ProductEntity> getProducts() {
        return products;
    }

    public void setProducts(Set<ProductEntity> products) {
        this.products = products;
    }

    // Helper methods for bidirectional relationship management
    public void addProduct(ProductEntity product) {
        products.add(product);
        product.setCategoryType(this);
    }

    public void removeProduct(ProductEntity product) {
        products.remove(product);
        product.setCategoryType(null);
    }

    // Pre-persist and pre-update callbacks
    @PrePersist
    protected void onCreate() {
        createdDate = new Timestamp(System.currentTimeMillis());
    }

    @PreUpdate
    protected void onUpdate() {
        modifiedDate = new Timestamp(System.currentTimeMillis());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CategoryTypeEntity)) return false;
        CategoryTypeEntity that = (CategoryTypeEntity) o;
        return id != null && id.equals(that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "CategoryTypeEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", categoryId=" + categoryId +
                ", status=" + status +
                '}';
    }
}
