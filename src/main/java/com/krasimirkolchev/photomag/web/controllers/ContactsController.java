package com.krasimirkolchev.photomag.web.controllers;

import com.krasimirkolchev.photomag.models.bindingModels.SendEmailBindingModel;
import com.krasimirkolchev.photomag.services.EmailService;
import com.krasimirkolchev.photomag.validation.SendEmailValidation;
import com.krasimirkolchev.photomag.web.annotations.PageTitle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.mail.MessagingException;
import java.io.IOException;

@Controller
@RequestMapping("/contacts")
public class ContactsController {
    private final EmailService emailService;
    private final SendEmailValidation emailValidation;

    @Autowired
    public ContactsController(EmailService emailService, SendEmailValidation emailValidation) {
        this.emailService = emailService;
        this.emailValidation = emailValidation;
    }

    @GetMapping("")
    @PageTitle("Contacts")
    public String contacts(Model model) {
        if (!model.containsAttribute("sendEmailBindingModel")) {
            model.addAttribute("sendEmailBindingModel", new SendEmailBindingModel());
        }

        return "contacts";
    }

    @PostMapping("/send")
    public String sendEmail(@ModelAttribute("sendEmailBindingModel") SendEmailBindingModel sendEmailBindingModel,
                            BindingResult result, RedirectAttributes attributes) throws MessagingException, IOException {

        this.emailValidation.validate(sendEmailBindingModel, result);

        if (result.hasErrors()) {
            attributes.addFlashAttribute("sendEmailBindingModel", sendEmailBindingModel);
            attributes.addFlashAttribute("org.springframework.validation.BindingResult.sendEmailBindingModel"
                    , result);
            return "redirect:/contacts";
        }

        this.emailService.sendEmail(sendEmailBindingModel);
        attributes.addFlashAttribute("Success", "Email successfully has been sent!");

        return "redirect:/contacts";
    }
}
