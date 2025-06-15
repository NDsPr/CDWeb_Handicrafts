package com.handicrafts.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "order_details")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailEntity {

    @EmbeddedId
    private OrderDetailId id;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "reviewed")
    private Boolean reviewed = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("productId")
    @JoinColumn(name = "productId")
    private ProductEntity product;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("orderId")
    @JoinColumn(name = "orderId")
    private OrderEntity order;
}
