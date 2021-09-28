package com.krasimirkolchev.photomag.services;

import com.krasimirkolchev.photomag.models.bindingModels.SendEmailBindingModel;
import com.krasimirkolchev.photomag.models.serviceModels.VoucherServiceModel;

import javax.mail.MessagingException;
import java.io.IOException;

public interface EmailService {

    void sendEmail(SendEmailBindingModel sendEmailBindingModel) throws MessagingException, IOException;

    void sendVouchersByEmail(VoucherServiceModel voucherServiceModel) throws MessagingException;

    void sendExeptionOnMyEmail(String text);
}
