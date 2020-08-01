package com.krasimirkolchev.photomag.models.bindingModels;

import com.krasimirkolchev.photomag.models.serviceModels.ProductServiceModel;

import java.util.ArrayList;
import java.util.List;

public class BrandAddBindingModel {
    private String name;
    private List<ProductServiceModel> products;

    public BrandAddBindingModel() {
        this.products = new ArrayList<>();
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
