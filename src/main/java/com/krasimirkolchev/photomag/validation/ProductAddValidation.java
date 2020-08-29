package com.krasimirkolchev.photomag.validation;

import com.krasimirkolchev.photomag.models.bindingModels.ProductAddBindingModel;
import com.krasimirkolchev.photomag.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.MultipartFile;

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
            errors.rejectValue("name", "Product name must be between 12 and 60 symbols long!", "Product name must be between 12 and 60 symbols long!");
        }
        if (this.productRepository.existsByName(productAddBindingModel.getName())) {
            errors.rejectValue("name", "Product already exist!", "Product already exist!");
        }
        if (productAddBindingModel.getDescription().length() < 20) {
            errors.rejectValue("description", "Description must be at least 20 symbols long!", "Description must be at least 20 symbols long!");
        }
        if (productAddBindingModel.getQuantity() == null || productAddBindingModel.getQuantity() < 1) {
            errors.rejectValue("quantity", "Quantity must be more than 1!", "Quantity must be more than 1!");
        }
        if (productAddBindingModel.getPrice() == null || productAddBindingModel.getPrice() < 1.0) {
            errors.rejectValue("price", "Price must be more than 1.0!", "Price must be more than 1!");
        }
        if (productAddBindingModel.getProductCategory() == null || productAddBindingModel.getProductCategory().isBlank()) {
            errors.rejectValue("productCategory", "Product category must be selected!", "Product category must be selected!");
        }
        if (productAddBindingModel.getBrand() == null || productAddBindingModel.getBrand().isBlank()) {
            errors.rejectValue("brand", "Brand must be selected!", "Brand must be selected!");
        }
        if (productAddBindingModel.getProductGallery().get(0).getOriginalFilename().isEmpty()) {
            errors.rejectValue("productGallery", "Select images for the gallery!", "Select images for the gallery!");
        } else {
            for (MultipartFile file : productAddBindingModel.getProductGallery()) {
                if (file == null || file.isEmpty() || file.getOriginalFilename().length() == 0) {
                    errors.rejectValue("productGallery", "Some images are corrupted!", "Some images are corrupted!");
                }
            }
        }

    }
}
