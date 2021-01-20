package com.krasimirkolchev.photomag.validation;

import com.krasimirkolchev.photomag.models.bindingModels.ExpOrdersDatesBindingModel;
import com.krasimirkolchev.photomag.models.entities.Order;
import com.krasimirkolchev.photomag.models.serviceModels.OrderServiceModel;
import com.krasimirkolchev.photomag.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ExportOrdersValidation implements Validator {
    private final OrderRepository orderRepository;

    @Autowired
    public ExportOrdersValidation(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return ExpOrdersDatesBindingModel.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ExpOrdersDatesBindingModel expOrdersDatesBindingModel = (ExpOrdersDatesBindingModel) target;

        System.out.println("==>" + expOrdersDatesBindingModel.getExpFrom());
        System.out.println("==>" + expOrdersDatesBindingModel.getExpTo());

        if (expOrdersDatesBindingModel.getExpFrom().isBlank()) {
            errors.rejectValue("expFrom", "Please choose date!", "Please choose date!");
            return;
        } else if (expOrdersDatesBindingModel.getExpTo().isBlank()) {
            errors.rejectValue("expTo", "Please choose date!", "Please choose date!");
            return;
        }

        LocalDateTime from = LocalDateTime
                .parse(expOrdersDatesBindingModel.getExpFrom() + "T00:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        LocalDateTime to = LocalDateTime
                .parse(expOrdersDatesBindingModel.getExpTo() + "T23:59", DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        if (from.isAfter(LocalDateTime.now().plusDays(1L))) {
            errors.rejectValue("expFrom", "Date cannot be in the future!", "Date cannot be in the future!");
        } else if (to.isBefore(from)) {
            errors.rejectValue("expTo", "Date cannot be before Start date!", "Date cannot be before Start Date!");
        } else if (to.isAfter(LocalDateTime.now().plusDays(1L))) {
            errors.rejectValue("expTo", "Date cannot be after current date!", "Date cannot be after current date!");
        }

    }
}
