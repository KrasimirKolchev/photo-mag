package com.krasimirkolchev.photomag.validation;

import com.krasimirkolchev.photomag.models.bindingModels.SendEmailBindingModel;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import static com.krasimirkolchev.photomag.common.CommonMessages.*;

@Component
public class SendEmailValidation implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return SendEmailBindingModel.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        SendEmailBindingModel sendEmailBindingModel = (SendEmailBindingModel) target;

        if (sendEmailBindingModel.getSender().isBlank()) {
            errors.rejectValue("sender", EMAIL_SENDER_ERR, EMAIL_SENDER_ERR);
        }
        if (sendEmailBindingModel.getSubject().isBlank()) {
            errors.rejectValue("subject", EMAIL_SUBJECT_ERR, EMAIL_SUBJECT_ERR);
        }
        if (sendEmailBindingModel.getTextMessage().isBlank()) {
            errors.rejectValue("textMessage", EMAIL_MESSAGE_ERR, EMAIL_MESSAGE_ERR);
        }
    }
}
