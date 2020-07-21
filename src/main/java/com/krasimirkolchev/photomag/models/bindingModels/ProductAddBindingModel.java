package com.krasimirkolchev.photomag.models.bindingModels;

import com.krasimirkolchev.photomag.models.entities.enums.ProductCategory;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class ProductAddBindingModel {
    private String name;
    private String description;
    private Double price;
    private Integer quantity;
    private ProductCategory productCategory;
    private List<MultipartFile> productGallery;

    public ProductAddBindingModel() {
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

    public List<MultipartFile> getProductGallery() {
        return productGallery;
    }

    public void setProductGallery(List<MultipartFile> productGallery) {
        this.productGallery = productGallery;
    }
}
