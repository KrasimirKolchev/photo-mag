package com.krasimirkolchev.photomag.web.controllers;

import com.krasimirkolchev.photomag.error.*;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@Component
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ModelAndView handleExceptions(Exception ex) {
        ModelAndView model = new ModelAndView("error");
        model.addObject("error", ex.getMessage());
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
