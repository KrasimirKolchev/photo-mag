package com.krasimirkolchev.photomag.validation;

import com.krasimirkolchev.photomag.models.bindingModels.UserRegBindingModel;
import com.krasimirkolchev.photomag.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UserRegisterValidation implements Validator {
    private final UserRepository userRepository;

    @Autowired
    public UserRegisterValidation(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return UserRegBindingModel.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserRegBindingModel userRegBindingModel = (UserRegBindingModel) target;

        if (userRegBindingModel.getUsername().isBlank() || userRegBindingModel.getUsername().length() < 6 || userRegBindingModel.getUsername().length() > 16) {
            errors.rejectValue("username", "Username must be between 6 and 16 symbols!", "Username must be between 6 and 16 symbols");
        }
        if (userRegBindingModel.getPassword().length() < 6 || userRegBindingModel.getPassword().length() > 12) {
            errors.rejectValue("password", "Password must be between 6 and 12 characters", "Password must be between 6 and 12 characters");
        }
        if (!userRegBindingModel.getPassword().equals(userRegBindingModel.getConfirmPassword())) {
            errors.rejectValue("password", "Invalid Password", "Passwords do not match!");
        }
        if (this.userRepository.existsByUsername(userRegBindingModel.getUsername())) {
            errors.rejectValue("username", "Username already exists", "Username already exists!");
        }
        if (this.userRepository.existsByEmail(userRegBindingModel.getEmail())) {
            errors.rejectValue("email", "Email already exists", "Email already exists!");
        }
        if (userRegBindingModel.getFirstName().length() < 3 || userRegBindingModel.getFirstName().length() > 16) {
            errors.rejectValue("firstName", "First name must be between 3 and 16 symbols!", "First name must be between 3 and 16 symbols!");
        }
        if (userRegBindingModel.getLastName().length() < 3 || userRegBindingModel.getLastName().length() > 16) {
            errors.rejectValue("lastName", "Last name must be between 3 and 16 symbols!", "Last name must be between 3 and 16 symbols!");
        }

    }
}
