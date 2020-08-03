package com.krasimirkolchev.photomag.models.entities;

import javax.persistence.*;

@Entity
@Table(name = "cart_items")
public class CartItem extends BaseEntity {
    private Product item;
    private Integer quantity;
    private Double subTotal;

    public CartItem() {
    }

    @ManyToOne
    public Product getItem() {
        return item;
    }

    public void setItem(Product item) {
        this.item = item;
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
