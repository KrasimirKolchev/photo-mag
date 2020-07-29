package com.krasimirkolchev.photomag.models.serviceModels;

public class OrderItemServiceModel extends BaseServiceModel {
    private ProductServiceModel orderItem;
    private Integer quantity;
    private Double subTotal;

    public OrderItemServiceModel() {
    }

    public ProductServiceModel getOrderItem() {
        return orderItem;
    }

    public void setOrderItem(ProductServiceModel orderItem) {
        this.orderItem = orderItem;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(Double subTotal) {
        this.subTotal = subTotal;
    }
}
