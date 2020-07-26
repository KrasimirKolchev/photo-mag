package com.krasimirkolchev.photomag.models.serviceModels;

import com.krasimirkolchev.photomag.models.entities.Product;

public class CartItemServiceModel extends BaseServiceModel {
    private Product item;
    private Integer quantity;
    private Double subTotal;

    public CartItemServiceModel() {
    }

    public Product getItem() {
        return item;
    }

    public void setItem(Product item) {
        this.item = item;
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
