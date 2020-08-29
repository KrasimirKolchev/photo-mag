package com.krasimirkolchev.photomag.validation;

import com.krasimirkolchev.photomag.models.bindingModels.CategoryAddBindingModel;
import com.krasimirkolchev.photomag.repositories.ProductCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import static com.krasimirkolchev.photomag.common.CommonMessages.*;

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
            errors.rejectValue("name", CATEGORY_NAME_LENGTH, CATEGORY_NAME_LENGTH);
        }
        if (this.categoryRepository.existsByName(categoryAddBindingModel.getName())) {
            errors.rejectValue("name", CATEGORY_NAME_EXIST, CATEGORY_NAME_EXIST);
        }
        if (categoryAddBindingModel.getPhoto() == null || categoryAddBindingModel.getPhoto().isEmpty()
                || categoryAddBindingModel.getPhoto().getOriginalFilename().length() == 0) {
            errors.rejectValue("photo", CATEGORY_IMAGE_MISSING, CATEGORY_IMAGE_MISSING);
        }
    }
}
