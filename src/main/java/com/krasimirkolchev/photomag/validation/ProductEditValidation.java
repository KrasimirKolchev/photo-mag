package com.krasimirkolchev.photomag.validation;

import com.krasimirkolchev.photomag.models.bindingModels.ProductAddBindingModel;
import com.krasimirkolchev.photomag.models.bindingModels.ProductEditBindingModel;
import com.krasimirkolchev.photomag.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class ProductEditValidation implements Validator {
    private final ProductRepository productRepository;

    @Autowired
    public ProductEditValidation(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return ProductEditBindingModel.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ProductEditBindingModel productEditBindingModel = (ProductEditBindingModel) target;

        if (productEditBindingModel.getDescription().length() < 20) {
            errors.rejectValue("description", "Description must be at least 20 symbols long!", "Description must be at least 20 symbols long!");
        }
        if (productEditBindingModel.getQuantity() < 1 || productEditBindingModel.getQuantity() == null) {
            errors.rejectValue("quantity", "Quantity must be more than 1!", "Quantity must be more than 1!");
        }
        if (productEditBindingModel.getPrice() < 1.0 || productEditBindingModel.getPrice() == null) {
            errors.rejectValue("price", "Price must be more than 1.0!", "Price must be more than 1!");
        }
    }
}
