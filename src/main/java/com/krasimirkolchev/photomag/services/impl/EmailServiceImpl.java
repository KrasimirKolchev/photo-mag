package com.krasimirkolchev.photomag.services.impl;

import com.krasimirkolchev.photomag.models.bindingModels.SendEmailBindingModel;
import com.krasimirkolchev.photomag.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Component
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender mailSender;

    @Autowired
    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
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

        mailSender.send(message);
        this.returnConfirmationMail(sendEmailBindingModel.getSender());
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
