package com.krasimirkolchev.photomag.models.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "order_items")
public class OrderItems extends BaseEntity {
    private Product item;
    private Integer quantity;
    private Double subTotal;

    public OrderItems() {
    }

    @ManyToOne
    public Product getItem() {
        return item;
    }

    public void setItem(Product item) {
        this.item = item;
    }

    @Column(name = "quantity")
    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Column(name = "sub_total")
    public Double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(Double subTotal) {
        this.subTotal = subTotal;
    }
}
