package com.krasimirkolchev.photomag.models.serviceModels;

import com.krasimirkolchev.photomag.models.entities.enums.ProductCategory;

import java.util.List;

public class ProductServiceModel {
    private String name;
    private String description;
    private Double price;
    private Integer quantity;
    private ProductCategory productCategory;
    private List<String> productGallery;

    public ProductServiceModel() {
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

    public List<String> getProductGallery() {
        return productGallery;
    }

    public void setProductGallery(List<String> productGallery) {
        this.productGallery = productGallery;
    }
}
