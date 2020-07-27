package com.krasimirkolchev.photomag.models.serviceModels;

import java.time.LocalDateTime;
import java.util.List;

public class OrderServiceModel {
    private List<CartItemServiceModel> orderItems;
    private Double totalAmount;
    private UserServiceModel user;
    private LocalDateTime purchaseDateTime;
    private String chargeId;

    public OrderServiceModel() {
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

    public UserServiceModel getUser() {
        return user;
    }

    public void setUser(UserServiceModel user) {
        this.user = user;
    }

    public LocalDateTime getPurchaseDateTime() {
        return purchaseDateTime;
    }

    public void setPurchaseDateTime(LocalDateTime purchaseDateTime) {
        this.purchaseDateTime = purchaseDateTime;
    }

    public String getChargeId() {
        return chargeId;
    }

    public void setChargeId(String chargeId) {
        this.chargeId = chargeId;
    }
}
