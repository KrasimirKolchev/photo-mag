package com.krasimirkolchev.photomag.web;

import com.krasimirkolchev.photomag.models.bindingModels.OrderAddBindingModel;
import com.krasimirkolchev.photomag.models.serviceModels.OrderServiceModel;
import com.krasimirkolchev.photomag.models.serviceModels.UserServiceModel;
import com.krasimirkolchev.photomag.payment.ChargeRequest;
import com.krasimirkolchev.photomag.payment.Currency;
import com.krasimirkolchev.photomag.payment.StripeService;
import com.krasimirkolchev.photomag.services.OrderService;
import com.krasimirkolchev.photomag.services.UserService;
import com.stripe.exception.*;
import com.stripe.model.Charge;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/shopping-cart")
public class CheckoutController {
    private final StripeService stripeService;
    private final OrderService orderService;
    private final UserService userService;
    private final ModelMapper modelMapper;

    @Autowired
    public CheckoutController(StripeService stripeService, OrderService orderService, UserService userService, ModelMapper modelMapper) {
        this.stripeService = stripeService;
        this.orderService = orderService;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/charge")
    public String charge(ChargeRequest chargeRequest, Principal principal, OrderAddBindingModel orderAddBindingModel)
            throws CardException, APIException, AuthenticationException, InvalidRequestException, APIConnectionException {
        chargeRequest.setDescription("Example charge");
        chargeRequest.setCurrency(Currency.EUR);
        Charge charge = stripeService.charge(chargeRequest);

        OrderServiceModel orderServiceModel = this.modelMapper.map(orderAddBindingModel, OrderServiceModel.class);
        orderServiceModel.setUser(this.modelMapper
                .map(this.userService.getUserByUsername(principal.getName()), UserServiceModel.class));
        orderServiceModel.setChargeId(charge.getId());
        orderServiceModel.setPurchaseDateTime(LocalDateTime.now());

        return "redirect:/orders/all";
    }
}

