package com.krasimirkolchev.photomag.services.impl;

import com.krasimirkolchev.photomag.models.bindingModels.SendEmailBindingModel;
import com.krasimirkolchev.photomag.models.serviceModels.VoucherServiceModel;
import com.krasimirkolchev.photomag.services.EmailService;
import com.krasimirkolchev.photomag.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Arrays;
import java.util.List;

@Component
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender mailSender;
    private final UserService userService;
    private final ModelMapper modelMapper;

    @Autowired
    public EmailServiceImpl(JavaMailSender mailSender, UserService userService, ModelMapper modelMapper) {
        this.mailSender = mailSender;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @Override
    public void sendEmail(SendEmailBindingModel sendEmailBindingModel) throws MessagingException {

        MimeMessage message = this.mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom("photomagapp@gmail.com");
        helper.setTo("krasimir.kolchev2087@gmail.com");
        helper.setSubject("PhotoMag Application!!! Email from: " + sendEmailBindingModel
                .getSender() + "==> " + sendEmailBindingModel.getSubject());
        helper.setText(sendEmailBindingModel.getTextMessage());

        this.mailSender.send(message);
        this.returnConfirmationMail(sendEmailBindingModel.getSender());
    }

    @Override
    public void sendVouchersByEmail(VoucherServiceModel voucherServiceModel) throws MessagingException {
        MimeMessage message = this.mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, false);

        helper.setFrom("photomagapp@gmail.com");
        List<String> emailList = this.userService.getAllUsersEmail();
        //to check if email is sent
        emailList.add("krasimir.kolchev2087@gmail.com");
        helper.setTo(emailList.toArray(new String[0]));
        helper.setSubject("PhotoMag Application!!! Voucher");

        StringBuilder text = new StringBuilder();
        text.append(String.format("Voucher: %s", voucherServiceModel.getVoucherName()))
                .append(System.lineSeparator());
        text.append(String.format("Discount: %d %%", voucherServiceModel.getDiscountPercentage()))
                .append(System.lineSeparator());
        text.append(String.format("Valid From: %s", voucherServiceModel.getStartDate()))
                .append(System.lineSeparator());
        text.append(String.format("Valid To: %s", voucherServiceModel.getEndDate()))
                        .append(System.lineSeparator());

        helper.setText(text.toString());

        this.mailSender.send(message);
    }

    private void returnConfirmationMail(String sender) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("photomagapp@gmail.com");
        message.setTo(sender);
        message.setSubject("PhotoMag Application!!!");
        message.setText("You successfully send email to PhotoMag App. We will answer you soon!");

        this.mailSender.send(message);
    }
}
