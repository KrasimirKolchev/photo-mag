package com.krasimirkolchev.photomag.validation;

import com.krasimirkolchev.photomag.models.bindingModels.UserEditBindingModel;
import com.krasimirkolchev.photomag.models.entities.User;
import com.krasimirkolchev.photomag.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import static com.krasimirkolchev.photomag.common.CommonMessages.*;

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
            errors.rejectValue("oldPassword", USER_OLD_PASSWORD_NOT_MATCH, USER_OLD_PASSWORD_NOT_MATCH);
        }
        if (userEditBindingModel.getOldPassword().length() < 6 || userEditBindingModel.getOldPassword().length() > 12) {
            errors.rejectValue("oldPassword", USER_OLD_PASSWORD_LENGTH, USER_OLD_PASSWORD_LENGTH);
        }
        if (!userEditBindingModel.getPassword().isBlank()) {
            if (userEditBindingModel.getPassword().length() < 6 || userEditBindingModel.getPassword().length() > 12) {
                errors.rejectValue("password", USER_PASSWORD_LENGTH, USER_PASSWORD_LENGTH);
            }
            if (!userEditBindingModel.getPassword().equals(userEditBindingModel.getConfirmPassword()) && !userEditBindingModel.getPassword().isBlank()) {
                errors.rejectValue("password", USER_PASSWORD_ERR, USER_PASSWORD_ERR);
            }
        }
        if (!userEditBindingModel.getFirstName().isBlank() && userEditBindingModel.getFirstName().length() < 3 || userEditBindingModel.getFirstName().length() > 16) {
            errors.rejectValue("firstName", USER_FIRSTNAME_LENGTH, USER_FIRSTNAME_LENGTH);
        }
        if (!userEditBindingModel.getLastName().isBlank() && userEditBindingModel.getLastName().length() < 3 || userEditBindingModel.getLastName().length() > 16) {
            errors.rejectValue("lastName", USER_LASTNAME_LENGTH, USER_LASTNAME_LENGTH);
        }
    }
}
