package com.krasimirkolchev.photomag.models.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "order_items")
public class OrderItems extends BaseEntity {
    private Product orderItem;
    private Integer quantity;
    private Double subTotal;

    public OrderItems() {
    }

    @ManyToOne
    public Product getOrderItem() {
        return orderItem;
    }

    public void setOrderItem(Product orderItem) {
        this.orderItem = orderItem;
    }

    @Column(name = "quantity", nullable = false)
    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Column(name = "sub_total", nullable = false)
    public Double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(Double subTotal) {
        this.subTotal = subTotal;
    }
}
