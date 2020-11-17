package com.krasimirkolchev.photomag.validation;

import com.krasimirkolchev.photomag.models.bindingModels.UserRegBindingModel;
import com.krasimirkolchev.photomag.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import static com.krasimirkolchev.photomag.common.CommonMessages.*;

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
            errors.rejectValue("username", USER_USERNAME_LENGTH, USER_USERNAME_LENGTH);
        }
        if (userRegBindingModel.getPassword().length() < 6 || userRegBindingModel.getPassword().length() > 12) {
            errors.rejectValue("password", USER_PASSWORD_LENGTH, USER_PASSWORD_LENGTH);
        }
        if (!userRegBindingModel.getPassword().matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{6,12}$")) {
            errors.rejectValue("password", USER_PASSWORD_MATCHER, USER_PASSWORD_MATCHER);
        }
        if (!userRegBindingModel.getPassword().equals(userRegBindingModel.getConfirmPassword())) {
            errors.rejectValue("password", USER_PASSWORD_ERR, USER_PASSWORD_ERR);
        }
        if (this.userRepository.existsByUsername(userRegBindingModel.getUsername())) {
            errors.rejectValue("username", USER_USERNAME_EXIST, USER_USERNAME_EXIST);
        }
        if (this.userRepository.existsByEmail(userRegBindingModel.getEmail())) {
            errors.rejectValue("email", USER_EMAIL_EXIST, USER_EMAIL_EXIST);
        }
        if (userRegBindingModel.getFirstName().length() < 3 || userRegBindingModel.getFirstName().length() > 16) {
            errors.rejectValue("firstName", USER_FIRSTNAME_LENGTH, USER_FIRSTNAME_LENGTH);
        }
        if (userRegBindingModel.getLastName().length() < 3 || userRegBindingModel.getLastName().length() > 16) {
            errors.rejectValue("lastName", USER_LASTNAME_LENGTH, USER_LASTNAME_LENGTH);
        }
        if (userRegBindingModel.getFile() == null || userRegBindingModel.getFile().isEmpty()
                || userRegBindingModel.getFile().getOriginalFilename().length() == 0) {
            errors.rejectValue("file", USER_IMAGE_ERR, USER_IMAGE_ERR);
        }

    }
}
