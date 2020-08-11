package com.krasimirkolchev.photomag.services;

import com.krasimirkolchev.photomag.models.serviceModels.ProductCategoryServiceModel;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ProductCategoryService {

    List<ProductCategoryServiceModel> getAllCategories();

    ProductCategoryServiceModel getCategoryById(String id);

    ProductCategoryServiceModel addCategory(ProductCategoryServiceModel categoryServiceModel, MultipartFile file) throws IOException;

}
