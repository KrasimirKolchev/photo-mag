package com.krasimirkolchev.photomag.models.bindingModels;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

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
    @Positive(message = "Quantity must be positive number!")
    @Min(value = 1, message = "Quantity must be at least 1!")
    @Max(value = 5, message = "Quantity cannot be more than 5!")
    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
