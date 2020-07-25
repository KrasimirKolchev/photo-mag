package com.krasimirkolchev.photomag.models.bindingModels;

import javax.validation.constraints.NotNull;

public class CartItemAddBindModel {
    private String id;
    private Integer quantity;

    public CartItemAddBindModel() {
    }

    @NotNull
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @NotNull
    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
