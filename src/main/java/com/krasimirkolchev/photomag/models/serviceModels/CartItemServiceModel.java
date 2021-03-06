package com.krasimirkolchev.photomag.models.serviceModels;

public class CartItemServiceModel extends BaseServiceModel {
    private ProductServiceModel item;
    private Integer quantity;
    private Double subTotal;

    public CartItemServiceModel() {
    }

    public ProductServiceModel getItem() {
        return item;
    }

    public void setItem(ProductServiceModel item) {
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
