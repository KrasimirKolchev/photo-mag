package com.krasimirkolchev.photomag.web.controllers;

import com.krasimirkolchev.photomag.services.ProductService;
import com.krasimirkolchev.photomag.web.annotations.PageTitle;
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
    @PageTitle("Home")
    public String index(Model model) {
        if (!model.containsAttribute("products")) {
            model.addAttribute("products", this.productService.getAllProducts());
        }
        return "home";
    }

    @GetMapping("/home")
    public String home(Model model) {
        if (!model.containsAttribute("products")) {
            model.addAttribute("products", this.productService.getAllProducts());
        }
        return "home";
    }

    
}
