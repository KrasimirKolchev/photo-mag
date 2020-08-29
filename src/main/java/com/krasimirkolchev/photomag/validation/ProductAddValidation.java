package com.krasimirkolchev.photomag.validation;

import com.krasimirkolchev.photomag.models.bindingModels.ProductAddBindingModel;
import com.krasimirkolchev.photomag.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.MultipartFile;

import static com.krasimirkolchev.photomag.common.CommonMessages.*;

@Component
public class ProductAddValidation implements Validator {
    private final ProductRepository productRepository;

    @Autowired
    public ProductAddValidation(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return ProductAddBindingModel.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ProductAddBindingModel productAddBindingModel = (ProductAddBindingModel) target;

        if (productAddBindingModel.getName().length() < 12 || productAddBindingModel.getName().length() > 60) {
            errors.rejectValue("name", PRODUCT_NAME_LENGTH, PRODUCT_NAME_LENGTH);
        }
        if (this.productRepository.existsByName(productAddBindingModel.getName())) {
            errors.rejectValue("name", PRODUCT_NAME_EXIST, PRODUCT_NAME_EXIST);
        }
        if (productAddBindingModel.getDescription().length() < 20) {
            errors.rejectValue("description", PRODUCT_DESCRIPTION_LENGTH, PRODUCT_DESCRIPTION_LENGTH);
        }
        if (productAddBindingModel.getQuantity() == null || productAddBindingModel.getQuantity() < 1) {
            errors.rejectValue("quantity", PRODUCT_QUANTITY_ERR, PRODUCT_QUANTITY_ERR);
        }
        if (productAddBindingModel.getPrice() == null || productAddBindingModel.getPrice() < 1.0) {
            errors.rejectValue("price", PRODUCT_PRICE_ERR, PRODUCT_PRICE_ERR);
        }
        if (productAddBindingModel.getProductCategory() == null || productAddBindingModel.getProductCategory().isBlank()) {
            errors.rejectValue("productCategory", PRODUCT_CATEGORY_ERR, PRODUCT_CATEGORY_ERR);
        }
        if (productAddBindingModel.getBrand() == null || productAddBindingModel.getBrand().isBlank()) {
            errors.rejectValue("brand", PRODUCT_BRAND_ERR, PRODUCT_BRAND_ERR);
        }
        if (productAddBindingModel.getProductGallery().get(0).getOriginalFilename().isEmpty()) {
            errors.rejectValue("productGallery", PRODUCT_GALLERY_ERR, PRODUCT_GALLERY_ERR);
        } else {
            for (MultipartFile file : productAddBindingModel.getProductGallery()) {
                if (file == null || file.isEmpty() || file.getOriginalFilename().length() == 0) {
                    errors.rejectValue("productGallery", PRODUCT_GALLERY_IMAGES_ERR, PRODUCT_GALLERY_IMAGES_ERR);
                }
            }
        }

    }
}
