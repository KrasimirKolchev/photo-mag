package com.krasimirkolchev.photomag.validation;

import com.krasimirkolchev.photomag.models.bindingModels.AddressAddBindingModel;
import com.krasimirkolchev.photomag.repositories.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class AddressValidation implements Validator {
    private final AddressRepository addressRepository;

    @Autowired
    public AddressValidation(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return AddressAddBindingModel.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        AddressAddBindingModel addressAddBindingModel = (AddressAddBindingModel) target;

        if (addressAddBindingModel.getCountry().length() < 3 || addressAddBindingModel.getCountry().length() > 20) {
            errors.rejectValue("country", "Country name must be between 3 and 20 symbols!", "Country name must be between 3 and 20 symbols!");
        }
        if (addressAddBindingModel.getCity().length() < 3 || addressAddBindingModel.getCity().length() > 20) {
            errors.rejectValue("city", "City name must be between 3 and 20 symbols!", "City name must be between 3 and 20 symbols!");
        }
        if (addressAddBindingModel.getStreet().length() < 3 || addressAddBindingModel.getStreet().length() > 50) {
            errors.rejectValue("street", "Street name must be between 3 and 50 symbols!", "Street name must be between 3 and 50 symbols!");
        }
    }
}
