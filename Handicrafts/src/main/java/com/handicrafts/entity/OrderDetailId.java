package com.handicrafts.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailId implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "orderId")
    private Integer orderId;

    @Column(name = "productId")
    private Integer productId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrderDetailId that = (OrderDetailId) o;

        if (!orderId.equals(that.orderId)) return false;
        return productId.equals(that.productId);
    }

    @Override
    public int hashCode() {
        int result = orderId.hashCode();
        result = 31 * result + productId.hashCode();
        return result;
    }
}
