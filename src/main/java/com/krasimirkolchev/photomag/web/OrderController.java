package com.krasimirkolchev.photomag.web;

import com.krasimirkolchev.photomag.models.serviceModels.UserServiceModel;
import com.krasimirkolchev.photomag.services.OrderService;
import com.krasimirkolchev.photomag.services.UserService;
import org.apache.catalina.Role;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
    public String getAllOrders(Model model, Principal principal) {
        if (!model.containsAttribute("orders")) {
            UserServiceModel userServiceModel = this.modelMapper
                    .map(this.userService.getUserByUsername(principal.getName()), UserServiceModel.class);

            if (userServiceModel.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_USER"))) {
                model.addAttribute("orders", this.orderService
                        .getAllOrdersByUsername(userServiceModel.getUsername()));
            } else {
                model.addAttribute("orders", this.orderService.getAllOrders());
            }
        }

        return "orders-all";
    }

}
