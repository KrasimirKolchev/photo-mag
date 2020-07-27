package com.krasimirkolchev.photomag.models.entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "shopping_cart")
public class ShoppingCart extends BaseEntity {
    private List<CartItem> items;
    private Double totalCartAmount;

    public ShoppingCart() {
        this.items = new ArrayList<>();
        this.totalCartAmount = 0.0;
    }

    @ManyToMany
    public List<CartItem> getItems() {
        return items;
    }

    public void setItems(List<CartItem> items) {
        this.items = items;
    }

    @Column(name = "total_cart_amount")
    public Double getTotalCartAmount() {
        return totalCartAmount;
    }

    public void setTotalCartAmount(Double totalCartAmount) {
        this.totalCartAmount = totalCartAmount;
    }
}
