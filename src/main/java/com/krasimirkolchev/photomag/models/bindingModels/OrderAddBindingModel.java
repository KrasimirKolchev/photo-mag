package com.krasimirkolchev.photomag.models.bindingModels;

import com.krasimirkolchev.photomag.models.serviceModels.CartItemServiceModel;
import com.krasimirkolchev.photomag.models.serviceModels.OrderItemServiceModel;

import java.util.List;

public class OrderAddBindingModel {
    private List<String> orderItems;
    private Double totalAmount;

    public OrderAddBindingModel() {
    }

    public List<String> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<String> orderItems) {
        this.orderItems = orderItems;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

}
