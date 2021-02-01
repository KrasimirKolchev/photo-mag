package com.krasimirkolchev.photomag.services;

import com.krasimirkolchev.photomag.models.bindingModels.SendEmailBindingModel;

import javax.mail.MessagingException;
import java.io.IOException;

public interface EmailService {

    void sendEmail(SendEmailBindingModel sendEmailBindingModel) throws MessagingException, IOException;
}
