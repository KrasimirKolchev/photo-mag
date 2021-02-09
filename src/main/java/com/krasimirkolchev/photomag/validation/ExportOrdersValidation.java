package com.krasimirkolchev.photomag.validation;

import com.krasimirkolchev.photomag.models.bindingModels.ExpOrdersDatesBindingModel;
import com.krasimirkolchev.photomag.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.krasimirkolchev.photomag.common.CommonMessages.*;

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

        LocalDateTime from = LocalDateTime
                .parse(expOrdersDatesBindingModel.getExpFrom() + "T00:00:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        LocalDateTime to = LocalDateTime
                .parse(expOrdersDatesBindingModel.getExpTo() + "T23:59:59", DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        if (expOrdersDatesBindingModel.getExpFrom().isBlank()) {
            errors.rejectValue("expFrom", EXP_ORDER_SELECT_DATE, EXP_ORDER_SELECT_DATE);
        }
        if (expOrdersDatesBindingModel.getExpTo().isBlank()) {
            errors.rejectValue("expTo", EXP_ORDER_SELECT_DATE, EXP_ORDER_SELECT_DATE);
        }
        if (from.isAfter(LocalDateTime.now())) {
            errors.rejectValue("expFrom", EXP_ORDER_DATE_FUTURE, EXP_ORDER_DATE_FUTURE);
        }
        if (to.isBefore(from)) {
            errors.rejectValue("expTo", EXP_ORDER_DATE_BEFORE, EXP_ORDER_DATE_BEFORE);
        }
        if (to.isAfter(LocalDateTime.now().plusDays(1L))) {
            errors.rejectValue("expTo", EXP_ORDER_DATE_AFTER, EXP_ORDER_DATE_AFTER);
        }

    }
}
