package com.krasimirkolchev.photomag.models.bindingModels;

import com.krasimirkolchev.photomag.models.serviceModels.CartItemServiceModel;

import java.util.List;

public class OrderAddBindingModel {
    private List<CartItemServiceModel> orderItems;
    private Double totalAmount;

    public OrderAddBindingModel() {
    }

    public List<CartItemServiceModel> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<CartItemServiceModel> orderItems) {
        this.orderItems = orderItems;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

}
