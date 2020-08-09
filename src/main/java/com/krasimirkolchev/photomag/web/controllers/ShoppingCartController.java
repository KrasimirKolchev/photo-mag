package com.krasimirkolchev.photomag.web.controllers;

import com.krasimirkolchev.photomag.models.bindingModels.AddressGetBindingModel;
import com.krasimirkolchev.photomag.models.bindingModels.CartItemAddBindModel;
import com.krasimirkolchev.photomag.models.serviceModels.ShoppingCartServiceModel;
import com.krasimirkolchev.photomag.models.serviceModels.UserServiceModel;
import com.krasimirkolchev.photomag.payment.Currency;
import com.krasimirkolchev.photomag.services.ShoppingCartService;
import com.krasimirkolchev.photomag.services.UserService;
import com.krasimirkolchev.photomag.web.annotations.PageTitle;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.security.Principal;

@Controller
public class ShoppingCartController {
    private static final String STRIPE_PUBLIC_KEY =
            "pk_test_51H6HJBKfodfbToz72uIVEVq5B6FF5GGRtEqYU3eOxQFlntqhki9gGFpZZIhTZ2IabsRaooc2O2v6Ha2A1EPCtAAu0099yu2S3i";


    //    @Value("${STRIPE_PUBLIC_KEY}") for env variable
    @Value(STRIPE_PUBLIC_KEY)
    private String stripePublicKey;

    private final ShoppingCartService shoppingCartService;
    private final UserService userService;
    private final ModelMapper modelMapper;

    @Autowired
    public ShoppingCartController(ShoppingCartService shoppingCartService, UserService userService, ModelMapper modelMapper) {
        this.shoppingCartService = shoppingCartService;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/shopping-cart")
    @PageTitle("Shopping cart")
    @PreAuthorize("isAuthenticated()")
    public String shoppingCart(Model model, Principal principal) {
        if (!model.containsAttribute("shoppingCart")) {
            UserServiceModel user = this.modelMapper
                    .map(this.userService.getUserByUsername(principal.getName()), UserServiceModel.class);
            model.addAttribute("addresses", user.getAddresses());
            model.addAttribute("shoppingCart", user.getShoppingCart());
            model.addAttribute("addressGetBindingModel", new AddressGetBindingModel());
            int amount = (int) (user.getShoppingCart().getTotalCartAmount() * 100);

            model.addAttribute("amount", amount); // in cents
            model.addAttribute("stripePublicKey", stripePublicKey);
            model.addAttribute("currency", Currency.BGN);
        }
        return "shopping-cart";
    }

    @GetMapping("/shopping-cart/remove")
    @PreAuthorize("isAuthenticated()")
    public String shoppingCartRemove(@RequestParam(name = "id") String productId, Principal principal) {
        this.shoppingCartService.removeItemFromCart(productId, principal.getName());
        return "redirect:/shopping-cart";
    }

    @PostMapping("/shopping-cart/add")
    @PreAuthorize("isAuthenticated()")
    public String addToCart(@Valid @ModelAttribute("cartItem") CartItemAddBindModel cartItemAddBindModel,
                            BindingResult result, RedirectAttributes attributes, Principal principal) {

        System.out.println();
        if (result.hasErrors()) {
            attributes.addFlashAttribute("cartItem", cartItemAddBindModel);
            return "redirect:/products/details/" + cartItemAddBindModel.getId();
        }

        this.shoppingCartService.addItemToCart(cartItemAddBindModel, principal.getName());

        return "redirect:/categories";
    }
}
