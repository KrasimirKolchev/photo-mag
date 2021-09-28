package com.krasimirkolchev.photomag.web.controllers;

import com.krasimirkolchev.photomag.error.*;
import com.krasimirkolchev.photomag.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import java.sql.SQLException;
import java.util.Arrays;

@Component
@ControllerAdvice
public class GlobalExceptionHandler {
    private final EmailService emailService;

    @Autowired
    public GlobalExceptionHandler(EmailService emailService) {
        this.emailService = emailService;
    }

    @ExceptionHandler({Exception.class, SQLException.class})
    public ModelAndView handleExceptions(Exception ex) {
        ModelAndView model = new ModelAndView("error");
        model.addObject("error", ex.getMessage());
        //to know if some issue happens
        this.emailService.sendExeptionOnMyEmail(ex.getMessage() + "--->" + Arrays.toString(ex.getStackTrace()));

        ex.printStackTrace();
        return model;
    }

    @ExceptionHandler({AddressNotFoundException.class, BrandNotFoundException.class, UserNotFoundException.class,
            ProductCategoryNotFoundException.class, ProductNotFoundException.class})
    public ModelAndView handleCustomExceptions(BaseException ex) {
        ModelAndView model = new ModelAndView("error");
        model.addObject("error", ex.getMessage());
        model.addObject("code", ex.getStatus());
        return model;
    }

}
