package com.krasimirkolchev.photomag.models.serviceModels;

import java.util.ArrayList;
import java.util.List;

public class ShoppingCartServiceModel extends BaseServiceModel {
    private List<CartItemServiceModel> items;
    private Double totalCartAmount;

    public ShoppingCartServiceModel() {
        this.items = new ArrayList<>();
    }

    public List<CartItemServiceModel> getItems() {
        return items;
    }

    public void setItems(List<CartItemServiceModel> items) {
        this.items = items;
    }

    public Double getTotalCartAmount() {
        return totalCartAmount;
    }

    public void setTotalCartAmount(Double totalCartAmount) {
        this.totalCartAmount = totalCartAmount;
    }
}
