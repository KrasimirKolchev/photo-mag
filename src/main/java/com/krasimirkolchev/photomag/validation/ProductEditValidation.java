package com.krasimirkolchev.photomag.validation;

import com.krasimirkolchev.photomag.models.bindingModels.ProductEditBindingModel;
import com.krasimirkolchev.photomag.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import static com.krasimirkolchev.photomag.common.CommonMessages.*;

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
            errors.rejectValue("description", PRODUCT_DESCRIPTION_LENGTH, PRODUCT_DESCRIPTION_LENGTH);
        }
        if (productEditBindingModel.getQuantity() < 1 || productEditBindingModel.getQuantity() == null) {
            errors.rejectValue("quantity", PRODUCT_QUANTITY_ERR, PRODUCT_QUANTITY_ERR);
        }
        if (productEditBindingModel.getPrice() < 1.0 || productEditBindingModel.getPrice() == null) {
            errors.rejectValue("price", PRODUCT_PRICE_ERR, PRODUCT_PRICE_ERR);
        }
    }
}
