package com.krasimirkolchev.photomag.models.bindingModels;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class ProductEditBindingModel {
    private String id;
    private String name;
    private String description;
    private Double price;
    private Integer quantity;

    public ProductEditBindingModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NotBlank(message = "Description cannot be empty!")
    @Length(min = 20, message = "Description must be at least 20 symbols long!")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @NotNull(message = "Price cannot be empty")
    @Positive(message = "Price should be positive number!")
    @Min(value = 1, message = "Price cannot be less than 1!")
    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @NotNull(message = "Quantity cannot be empty!")
    @Positive(message = "Quantity should be positive number!")
    @Min(value = 1, message = "Quantity must be more than 1!")
    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

}
