package com.krasimirkolchev.photomag.web.controllers;

import com.krasimirkolchev.photomag.services.ProductService;
import com.krasimirkolchev.photomag.web.annotations.PageTitle;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    private final ProductService productService;

    public HomeController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/")
    @PageTitle("Index")
    public String index(Model model) {
        if (!model.containsAttribute("products")) {
            model.addAttribute("products", this.productService.getAllActiveProducts());
        }
        return "home";
    }

    @GetMapping("/home")
    @PageTitle("Home")
    @PreAuthorize("isAuthenticated()")
    public String home(Model model) {
        if (!model.containsAttribute("products")) {
            model.addAttribute("products", this.productService.getAllActiveProducts());
        }
        return "home";
    }

    @GetMapping("/about")
    @PageTitle("About")
    public String about() {
        return "about";
    }

    @GetMapping("/contacts")
    @PageTitle("Contacts")
    public String contacts() {
        return "contacts";
    }


}
