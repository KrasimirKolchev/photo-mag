package com.krasimirkolchev.photomag.web.controllers;

import com.krasimirkolchev.photomag.models.serviceModels.OrderServiceModel;
import com.krasimirkolchev.photomag.payment.ChargeRequest;
import com.krasimirkolchev.photomag.payment.Currency;
import com.krasimirkolchev.photomag.payment.StripeService;
import com.krasimirkolchev.photomag.services.OrderService;
import com.stripe.exception.*;
import com.stripe.model.Charge;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/shopping-cart")
public class CheckoutController {
    private final StripeService stripeService;
    private final OrderService orderService;

    @Autowired
    public CheckoutController(StripeService stripeService, OrderService orderService) {
        this.stripeService = stripeService;
        this.orderService = orderService;
    }

    @PostMapping("/charge")
    public String charge(ChargeRequest chargeRequest, Principal principal)
            throws CardException, APIException, AuthenticationException, InvalidRequestException, APIConnectionException {
        chargeRequest.setDescription("Example charge");
        chargeRequest.setCurrency(Currency.BGN);
        Charge charge = this.stripeService.charge(chargeRequest);


        OrderServiceModel orderServiceModel = this.orderService.generateOrder(charge, principal);

        return "redirect:/orders/all";
    }

    @ExceptionHandler(StripeException.class)
    public String handleError(Model model, StripeException ex) {
        model.addAttribute("error", ex.getMessage());
        return "redirect:/shopping-cart";
    }

}

