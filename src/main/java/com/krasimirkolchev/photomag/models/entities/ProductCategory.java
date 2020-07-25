package com.krasimirkolchev.photomag.models.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "categories")
public class ProductCategory extends BaseEntity {
    private String name;
    private String photo;

    public ProductCategory() {
    }

    public ProductCategory(String name, String photo) {
        this.name = name;
        this.photo = photo;
    }

    @Column(name = "name", unique = true, nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "photo", nullable = false)
    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }


}
