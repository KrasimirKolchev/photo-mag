package com.krasimirkolchev.photomag.web.controllers;

import com.krasimirkolchev.photomag.models.bindingModels.AddressGetBindingModel;
import com.krasimirkolchev.photomag.models.serviceModels.OrderServiceModel;
import com.krasimirkolchev.photomag.models.serviceModels.UserServiceModel;
import com.krasimirkolchev.photomag.payment.ChargeRequest;
import com.krasimirkolchev.photomag.payment.Currency;
import com.krasimirkolchev.photomag.payment.StripeService;
import com.krasimirkolchev.photomag.services.OrderService;
import com.krasimirkolchev.photomag.services.UserService;
import com.krasimirkolchev.photomag.web.annotations.PageTitle;
import com.stripe.exception.*;
import com.stripe.model.Charge;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.security.Principal;

@Controller
public class CheckoutController {
    private static final String STRIPE_PUBLIC_KEY =
            "pk_test_51H6HJBKfodfbToz72uIVEVq5B6FF5GGRtEqYU3eOxQFlntqhki9gGFpZZIhTZ2IabsRaooc2O2v6Ha2A1EPCtAAu0099yu2S3i";


    //    @Value("${STRIPE_PUBLIC_KEY}")
    @Value(STRIPE_PUBLIC_KEY)
    private String stripePublicKey;
    private final StripeService stripeService;
    private final OrderService orderService;
    private final UserService userService;
    private final ModelMapper modelMapper;


    @Autowired
    public CheckoutController(StripeService stripeService, OrderService orderService, UserService userService,
                              ModelMapper modelMapper) {
        this.stripeService = stripeService;
        this.orderService = orderService;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/checkout")
    @PageTitle("Checkout")
    @PreAuthorize("isAuthenticated()")
    public String checkoutConf(@ModelAttribute("addressGetBindingModel") AddressGetBindingModel addressGetBindingModel,
                               Principal principal, Model model, HttpSession session) {
        UserServiceModel user = this.modelMapper
                .map(this.userService.getUserByUsername(principal.getName()), UserServiceModel.class);
        int amount = (int) (user.getShoppingCart().getTotalCartAmount() * 100);
        session.setAttribute("addressId", addressGetBindingModel.getAddressId());
        model.addAttribute("amount", amount); // in cents
        model.addAttribute("stripePublicKey", stripePublicKey);
        model.addAttribute("currency", Currency.BGN);

        return "checkout";
    }

    @PostMapping("/charge")
    @PreAuthorize("isAuthenticated()")
    public String charge(ChargeRequest chargeRequest, Principal principal, HttpSession session)
            throws CardException, APIException, AuthenticationException, InvalidRequestException, APIConnectionException {
        chargeRequest.setDescription("Example charge");
        chargeRequest.setCurrency(Currency.BGN);
        Charge charge = this.stripeService.charge(chargeRequest);

        OrderServiceModel orderServiceModel = this.orderService
                .generateOrder(charge, principal, session.getAttribute("addressId").toString());

        return "redirect:/orders/all";
    }

    @ExceptionHandler(StripeException.class)
    public String handleError(Model model, StripeException ex) {
        model.addAttribute("errorMsg", ex.getMessage());
        return "redirect:/shopping-cart";
    }

}

