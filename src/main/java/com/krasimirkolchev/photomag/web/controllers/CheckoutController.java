package com.krasimirkolchev.photomag.web.controllers;

import com.krasimirkolchev.photomag.models.bindingModels.OrderDetailsGetBindingModel;
import com.krasimirkolchev.photomag.models.serviceModels.OrderServiceModel;
import com.krasimirkolchev.photomag.models.serviceModels.UserServiceModel;
import com.krasimirkolchev.photomag.models.serviceModels.VoucherServiceModel;
import com.krasimirkolchev.photomag.payment.ChargeRequest;
import com.krasimirkolchev.photomag.payment.Currency;
import com.krasimirkolchev.photomag.payment.StripeService;
import com.krasimirkolchev.photomag.services.DiscountService;
import com.krasimirkolchev.photomag.services.OrderService;
import com.krasimirkolchev.photomag.services.ShoppingCartService;
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

    @Value("${stripe-public-key}")
    private String stripePublicKey;

    private final StripeService stripeService;
    private final OrderService orderService;
    private final UserService userService;
    private final DiscountService discountService;
    private final ModelMapper modelMapper;

    @Autowired
    public CheckoutController(StripeService stripeService, OrderService orderService, UserService userService,
                              DiscountService discountService, ModelMapper modelMapper) {
        this.stripeService = stripeService;
        this.orderService = orderService;
        this.userService = userService;
        this.discountService = discountService;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/checkout")
    @PageTitle("Checkout")
    @PreAuthorize("isAuthenticated()")
    public String checkoutConf(@ModelAttribute("orderDetailsGetBindingModel") OrderDetailsGetBindingModel orderDetailsGetBindingModel,
                               Principal principal, Model model, HttpSession session) {


        if (!orderDetailsGetBindingModel.getVoucherName().isEmpty()
                && this.discountService.isValidVoucher(orderDetailsGetBindingModel.getVoucherName())) {
            VoucherServiceModel voucherServiceModel = this.discountService
                    .getVoucher(orderDetailsGetBindingModel.getVoucherName());

            this.discountService.useVoucher(principal.getName(), voucherServiceModel);
        }

        UserServiceModel user = this.modelMapper
                .map(this.userService.getUserByUsername(principal.getName()), UserServiceModel.class);

        int amount = (int) (user.getShoppingCart().getTotalCartAmount() * 100);
        session.setAttribute("addressId", orderDetailsGetBindingModel.getAddressId());
        model.addAttribute("totalAmount", user.getShoppingCart().getTotalCartAmount()); //to show in browser
        model.addAttribute("amount", amount); // in cents for Stripe
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

