package com.krasimirkolchev.photomag.models.bindingModels;

import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

public class ProductAddBindingModel {
    private String name;
    private String description;
    private Double price;
    private Integer quantity;
    private String productCategory;
    private List<MultipartFile> productGallery;

    public ProductAddBindingModel() {
    }

    @NotBlank(message = "")
    @Length(min = 12, max = 60, message = "")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NotBlank(message = "")
    @Length(min = 20, message = "")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @NotNull(message = "")
    @Positive(message = "")
    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @NotNull(message = "")
    @Positive(message = "")
    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @NotBlank(message = "")
    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    @NotNull(message = "")
    public List<MultipartFile> getProductGallery() {
        return productGallery;
    }

    public void setProductGallery(List<MultipartFile> productGallery) {
        this.productGallery = productGallery;
    }
}
