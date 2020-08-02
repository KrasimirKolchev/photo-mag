package com.krasimirkolchev.photomag.models.entities;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "brands")
public class Brand extends BaseEntity {
    private String name;
    private List<Product> products;

    public Brand() {
    }

    @Column(name = "brand", unique = true, nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @OneToMany(mappedBy = "brand", fetch = FetchType.EAGER)
    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}
