package com.handicrafts.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.Formula;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "products")
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 250)
    private String name;

    @Column(columnDefinition = "text", nullable = false)
    private String description;

    @Column(name = "categoryTypeId", nullable = false)
    private Integer categoryTypeId;

    @Column(name = "originalPrice", nullable = false)
    private Double originalPrice;

    @Column(name = "discountPrice", nullable = false)
    private Double discountPrice;

    @Column(name = "discountPercent", nullable = false)
    private Double discountPercent;

    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "soldQuantity")
    private Integer soldQuantity;

    @Column(name = "avgRate")
    private Double avgRate;

    @Column(name = "numReviews")
    private Integer numReviews;

    @Column(length = 30, nullable = false)
    private String size;

    @Column(name = "otherSpec", length = 200)
    private String otherSpec;

    @Column(columnDefinition = "text", nullable = false)
    private String keyword;

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
    @JoinColumn(name = "categoryTypeId", referencedColumnName = "id", insertable = false, updatable = false)
    private CategoryTypeEntity categoryType;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<OrderDetailEntity> orderDetails = new HashSet<>();

    // Default constructor
    public ProductEntity() {
    }

    // Constructor with essential fields
    public ProductEntity(String name, String description, Integer categoryTypeId, Double originalPrice, Integer quantity) {
        this.name = name;
        this.description = description;
        this.categoryTypeId = categoryTypeId;
        this.originalPrice = originalPrice;
        this.quantity = quantity;
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

    public Integer getCategoryTypeId() {
        return categoryTypeId;
    }

    public void setCategoryTypeId(Integer categoryTypeId) {
        this.categoryTypeId = categoryTypeId;
    }

    public Double getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(Double originalPrice) {
        this.originalPrice = originalPrice;
    }

    public Double getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(Double discountPrice) {
        this.discountPrice = discountPrice;
    }

    public Double getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(Double discountPercent) {
        this.discountPercent = discountPercent;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getSoldQuantity() {
        return soldQuantity;
    }

    public void setSoldQuantity(Integer soldQuantity) {
        this.soldQuantity = soldQuantity;
    }

    public Double getAvgRate() {
        return avgRate;
    }

    public void setAvgRate(Double avgRate) {
        this.avgRate = avgRate;
    }

    public Integer getNumReviews() {
        return numReviews;
    }

    public void setNumReviews(Integer numReviews) {
        this.numReviews = numReviews;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getOtherSpec() {
        return otherSpec;
    }

    public void setOtherSpec(String otherSpec) {
        this.otherSpec = otherSpec;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
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

    public CategoryTypeEntity getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(CategoryTypeEntity categoryType) {
        this.categoryType = categoryType;
    }

    public Set<OrderDetailEntity> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(Set<OrderDetailEntity> orderDetails) {
        this.orderDetails = orderDetails;
    }

    // Helper methods for bidirectional relationship management
    public void addOrderDetail(OrderDetailEntity orderDetail) {
        orderDetails.add(orderDetail);
        orderDetail.setProduct(this);
    }

    public void removeOrderDetail(OrderDetailEntity orderDetail) {
        orderDetails.remove(orderDetail);
        orderDetail.setProduct(null);
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
        if (!(o instanceof ProductEntity)) return false;
        ProductEntity product = (ProductEntity) o;
        return id != null && id.equals(product.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "ProductEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", originalPrice=" + originalPrice +
                ", discountPrice=" + discountPrice +
                ", quantity=" + quantity +
                '}';
    }
}
