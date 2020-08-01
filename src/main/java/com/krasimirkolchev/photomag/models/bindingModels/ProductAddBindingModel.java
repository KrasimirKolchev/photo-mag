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

    @NotBlank(message = "Product name cannot be empty!")
    @Length(min = 12, max = 60, message = "Product name must be between 12 and 60 symbols long!")
    public String getName() {
        return name;
    }

    @NotBlank(message = "Make cannot be empty!")
    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
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

    @NotBlank(message = "Product category must be selected!")
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
