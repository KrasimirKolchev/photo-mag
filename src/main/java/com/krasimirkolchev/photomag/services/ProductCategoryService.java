package com.krasimirkolchev.photomag.services;

import com.krasimirkolchev.photomag.models.bindingModels.CategoryAddBindingModel;
import com.krasimirkolchev.photomag.models.entities.ProductCategory;
import com.krasimirkolchev.photomag.models.serviceModels.ProductCategoryServiceModel;

import java.io.IOException;
import java.util.List;

public interface ProductCategoryService {

    List<ProductCategoryServiceModel> getAllCategories();

    ProductCategory getCategoryById(String id);

    void addCategory(CategoryAddBindingModel categoryAddBindingModel) throws IOException;

    ProductCategoryServiceModel getCategoryByName(String category);
}
