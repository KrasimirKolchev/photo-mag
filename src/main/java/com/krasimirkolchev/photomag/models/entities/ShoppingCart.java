package com.krasimirkolchev.photomag.models.entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "shopping_cart")
public class ShoppingCart extends BaseEntity {
    private List<CartItem> cartItem;
    private Double totalCartAmount;

    public ShoppingCart() {
        this.cartItem = new ArrayList<>();
    }

    @ManyToMany(targetEntity = CartItem.class, cascade = CascadeType.MERGE)
    public List<CartItem> getCartItem() {
        return cartItem;
    }

    public void setCartItem(List<CartItem> cartItem) {
        this.cartItem = cartItem;
    }

    @Column(name = "total_cart_amount")
    public Double getTotalCartAmount() {
        return totalCartAmount;
    }

    public void setTotalCartAmount(Double totalCartAmount) {
        this.totalCartAmount = totalCartAmount;
    }
}
