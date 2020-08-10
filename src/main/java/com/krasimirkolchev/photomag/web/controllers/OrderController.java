package com.krasimirkolchev.photomag.web.controllers;

import com.krasimirkolchev.photomag.models.bindingModels.AddressAddBindingModel;
import com.krasimirkolchev.photomag.models.bindingModels.AddressGetBindingModel;
import com.krasimirkolchev.photomag.models.serviceModels.UserServiceModel;
import com.krasimirkolchev.photomag.services.OrderService;
import com.krasimirkolchev.photomag.services.UserService;
import com.krasimirkolchev.photomag.web.annotations.PageTitle;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.security.Principal;

@Controller
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;
    private final UserService userService;
    private final ModelMapper modelMapper;

    @Autowired
    public OrderController(OrderService orderService, UserService userService, ModelMapper modelMapper) {
        this.orderService = orderService;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/all")
    @PageTitle("All orders")
    @PreAuthorize("isAuthenticated()")
    public String getAllOrders(Model model, Principal principal) {
        if (!model.containsAttribute("orders")) {
            UserServiceModel userServiceModel = this.modelMapper
                    .map(this.userService.getUserByUsername(principal.getName()), UserServiceModel.class);

            if (userServiceModel.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
                model.addAttribute("orders", this.orderService.getAllOrders());
            } else {
                model.addAttribute("orders", this.orderService
                        .getAllOrdersByUsername(userServiceModel.getUsername()));
            }
        }

        return "orders-all";
    }

    @GetMapping("/add-details")
    @PageTitle("Add order details")
    @PreAuthorize("isAuthenticated()")
    public String addOrderDetails(Model model, Principal principal) {
            if (!model.containsAttribute("addressGetBindingModel")) {
                model.addAttribute("addressGetBindingModel", new AddressGetBindingModel());
            }
            model.addAttribute("addresses", this.userService
                    .getUserByUsername(principal.getName()).getAddresses());
        return "add-order-details";
    }

    @PostMapping("/add-details")
    @PreAuthorize("isAuthenticated()")
    public String addOrderDetailsCOnf(@ModelAttribute("addressGetBindingModel") AddressGetBindingModel addressGetBindingModel,
                                      BindingResult result, RedirectAttributes attributes, Model model) {

        if (result.hasErrors()) {
            attributes.addFlashAttribute("addressGetBindingModel", addressGetBindingModel);
            attributes.addFlashAttribute("org.springframework.validation.BindingResult.addressGetBindingModel"
                    , result);
            return "redirect:/orders/add-details";
        }

        model.addAttribute("addressGetBindingModel", addressGetBindingModel);
        return "redirect:/checkout";
    }

}
