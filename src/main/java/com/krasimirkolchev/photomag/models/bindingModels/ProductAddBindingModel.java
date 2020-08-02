package com.krasimirkolchev.photomag.models.bindingModels;

import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.*;
import java.util.List;

public class ProductAddBindingModel {
    private String name;
    private String brand;
    private String description;
    private Double price;
    private Integer quantity;
    private String productCategory;
    private List<MultipartFile> productGallery;

    public ProductAddBindingModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
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

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    @NotNull(message = "Please choose files for product gallery!")
    public List<MultipartFile> getProductGallery() {
        return productGallery;
    }

    public void setProductGallery(List<MultipartFile> productGallery) {
        this.productGallery = productGallery;
    }
}
