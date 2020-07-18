package com.krasimirkolchev.photomag.models.bondingModels;

import com.krasimirkolchev.photomag.models.entities.enums.ProductCategory;

public class ProductAddindingModel {
    private String name;
    private String description;
    private Double price;
    private Integer quantity;
    private ProductCategory productCategory;

    public ProductAddindingModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public ProductCategory getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(ProductCategory productCategory) {
        this.productCategory = productCategory;
    }
}
