package com.krasimirkolchev.photomag.validation;

import com.krasimirkolchev.photomag.models.bindingModels.BrandAddBindingModel;
import com.krasimirkolchev.photomag.repositories.BrandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class BrandAddValidation implements Validator {
    private final BrandRepository brandRepository;

    @Autowired
    public BrandAddValidation(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return BrandAddBindingModel.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        BrandAddBindingModel brandAddBindingModel = (BrandAddBindingModel) target;

        if (brandAddBindingModel.getName().length() < 4 || brandAddBindingModel.getName().length() > 20) {
            errors.rejectValue("name", "Brand name must be between 4 and 20 symbols!", "Brand name must be between 4 and 20 symbols!");
        }
        if (this.brandRepository.existsByName(brandAddBindingModel.getName())) {
            errors.rejectValue("name", "Brand already exist!", "Brand already exist!");
        }
    }
}
