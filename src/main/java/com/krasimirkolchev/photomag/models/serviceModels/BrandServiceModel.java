package com.krasimirkolchev.photomag.models.serviceModels;

import java.util.List;

public class BrandServiceModel extends BaseServiceModel {
    private String name;
    private List<ProductServiceModel> products;

    public BrandServiceModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ProductServiceModel> getProducts() {
        return products;
    }

    public void setProducts(List<ProductServiceModel> products) {
        this.products = products;
    }
}
