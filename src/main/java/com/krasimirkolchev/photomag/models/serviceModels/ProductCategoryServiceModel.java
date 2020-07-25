package com.krasimirkolchev.photomag.models.serviceModels;

public class ProductCategoryServiceModel extends BaseServiceModel {
    private String name;
    private String photo;

    public ProductCategoryServiceModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
