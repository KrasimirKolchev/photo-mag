package com.krasimirkolchev.photomag.web;

import com.krasimirkolchev.photomag.payment.ChargeRequest;
import com.krasimirkolchev.photomag.payment.Currency;
import com.krasimirkolchev.photomag.payment.StripeService;
import com.stripe.exception.*;
import com.stripe.model.Charge;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/shopping-cart")
public class CheckoutController {
    private final StripeService stripeService;

    @Autowired
    public CheckoutController(StripeService stripeService) {
        this.stripeService = stripeService;
    }

    @PostMapping("/charge")
    public String charge(ChargeRequest chargeRequest, Model model)
            throws CardException, APIException, AuthenticationException, InvalidRequestException, APIConnectionException {
        chargeRequest.setDescription("Example charge");
        chargeRequest.setCurrency(Currency.EUR);
        Charge charge = stripeService.charge(chargeRequest);
        model.addAttribute("id", charge.getId());
        model.addAttribute("status", charge.getStatus());
        model.addAttribute("chargeId", charge.getId());
        model.addAttribute("balance_transaction", charge.getBalanceTransaction());
        return "result";
    }
}

