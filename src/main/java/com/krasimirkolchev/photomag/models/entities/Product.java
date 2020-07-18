package com.krasimirkolchev.photomag.models.entities;

import com.krasimirkolchev.photomag.models.entities.enums.ProductCategory;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "products")
public class Product extends BaseEntity {
    private String name;
    private String description;
    private Double price;
    private Integer quantity;
    private ProductCategory productCategory;
    private List<Photo> productGallery;

    public Product() {
    }

    public Product(String name, String description, Double price, Integer quantity, ProductCategory productCategory) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.productCategory = productCategory;
    }

    @Column(name = "name",unique = true, nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "description", nullable = false)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "price", nullable = false)
    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @Column(name = "quantity", nullable = false)
    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Enumerated
    public ProductCategory getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(ProductCategory productCategory) {
        this.productCategory = productCategory;
    }

    @OneToMany
    public List<Photo> getProductGallery() {
        return productGallery;
    }

    public void setProductGallery(List<Photo> productGallery) {
        this.productGallery = productGallery;
    }
}
