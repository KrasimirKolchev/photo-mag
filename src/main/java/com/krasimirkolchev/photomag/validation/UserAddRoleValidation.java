package com.krasimirkolchev.photomag.validation;

import com.krasimirkolchev.photomag.models.bindingModels.UserRoleAddBindingModel;
import com.krasimirkolchev.photomag.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UserAddRoleValidation implements Validator {
    private final UserRepository userRepository;

    @Autowired
    public UserAddRoleValidation(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return UserRoleAddBindingModel.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserRoleAddBindingModel userRoleAddBindingModel = (UserRoleAddBindingModel) target;

        if (userRoleAddBindingModel.getUsername().isBlank()) {
            errors.rejectValue("username", "Select user!", "Select user!");
        }
        if (userRoleAddBindingModel.getRole() == null) {
            errors.rejectValue("role", "Select role!", "Select role!");
        }

    }
}
