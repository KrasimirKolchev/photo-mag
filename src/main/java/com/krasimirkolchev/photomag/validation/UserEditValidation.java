package com.krasimirkolchev.photomag.validation;

import com.krasimirkolchev.photomag.models.bindingModels.UserEditBindingModel;
import com.krasimirkolchev.photomag.models.entities.User;
import com.krasimirkolchev.photomag.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UserEditValidation implements Validator {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;

    @Autowired
    public UserEditValidation(UserRepository userRepository, BCryptPasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return UserEditBindingModel.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserEditBindingModel userEditBindingModel = (UserEditBindingModel) target;
        User user = this.userRepository.getOne(userEditBindingModel.getId());

        if (!this.encoder.matches(userEditBindingModel.getOldPassword(), user.getPassword())) {
            errors.rejectValue("oldPassword", "Old Password not match!", "Old Password not match!");
        }
        if (userEditBindingModel.getOldPassword().length() < 6 || userEditBindingModel.getOldPassword().length() > 12) {
            errors.rejectValue("oldPassword", "Old Password must be between 6 and 12 symbols!", "Old Password must be between 6 and 12 symbols");
        }
        if (!userEditBindingModel.getPassword().isBlank()){
            if (userEditBindingModel.getPassword().length() < 6 || userEditBindingModel.getPassword().length() > 12) {
                errors.rejectValue("password", "Password must be between 6 and 12 characters", "Password must be between 6 and 12 characters");
            }
            if (!userEditBindingModel.getPassword().equals(userEditBindingModel.getConfirmPassword()) && !userEditBindingModel.getPassword().isBlank()) {
                errors.rejectValue("password", "Invalid Password", "Passwords do not match!");
            }
        }
        if (!userEditBindingModel.getFirstName().isBlank() && userEditBindingModel.getFirstName().length() < 3 || userEditBindingModel.getFirstName().length() > 16) {
            errors.rejectValue("firstName", "First name must be between 3 and 16 symbols!", "First name must be between 3 and 16 symbols!");
        }
        if (!userEditBindingModel.getLastName().isBlank() && userEditBindingModel.getLastName().length() < 3 || userEditBindingModel.getLastName().length() > 16) {
            errors.rejectValue("lastName", "Last name must be between 3 and 16 symbols!", "Last name must be between 3 and 16 symbols!");
        }
    }
}
