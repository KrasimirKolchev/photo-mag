package com.krasimirkolchev.photomag.validation;

import com.krasimirkolchev.photomag.models.bindingModels.BrandAddBindingModel;
import com.krasimirkolchev.photomag.repositories.BrandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import static com.krasimirkolchev.photomag.common.CommonMessages.*;

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
            errors.rejectValue("name", BRAND_NAME_LENGTH, BRAND_NAME_LENGTH);
        }
        if (this.brandRepository.existsByName(brandAddBindingModel.getName())) {
            errors.rejectValue("name", BRAND_NAME_EXIST, BRAND_NAME_EXIST);
        }
    }
}
