package com.krasimirkolchev.photomag.validation;

import com.krasimirkolchev.photomag.models.bindingModels.CategoryAddBindingModel;
import com.krasimirkolchev.photomag.repositories.ProductCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class CategoryValidation implements Validator {
    private final ProductCategoryRepository categoryRepository;

    @Autowired
    public CategoryValidation(ProductCategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return CategoryAddBindingModel.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        CategoryAddBindingModel categoryAddBindingModel = (CategoryAddBindingModel) target;

        if (categoryAddBindingModel.getName().length() < 4 || categoryAddBindingModel.getName().length() > 20) {
            errors.rejectValue("name", "Category name must be between 4 and 20 symbols!", "Category name must be between 4 and 20 symbols!");
        }
        if (this.categoryRepository.existsByName(categoryAddBindingModel.getName())) {
            errors.rejectValue("name", "Category already exist!");
        }
    }
}
