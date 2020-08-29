package com.krasimirkolchev.photomag.validation;

import com.krasimirkolchev.photomag.models.bindingModels.AddressAddBindingModel;
import com.krasimirkolchev.photomag.repositories.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import static com.krasimirkolchev.photomag.common.CommonMessages.*;

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
            errors.rejectValue("country", COUNTRY_NAME_LENGTH, COUNTRY_NAME_LENGTH);
        }
        if (addressAddBindingModel.getCity().length() < 3 || addressAddBindingModel.getCity().length() > 20) {
            errors.rejectValue("city", CITY_NAME_LENGTH, CITY_NAME_LENGTH);
        }
        if (addressAddBindingModel.getStreet().length() < 3 || addressAddBindingModel.getStreet().length() > 50) {
            errors.rejectValue("street", STREET_NAME_LENGTH, STREET_NAME_LENGTH);
        }
    }
}
