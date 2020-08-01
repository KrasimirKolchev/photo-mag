package com.krasimirkolchev.photomag.web.controllers;

import com.krasimirkolchev.photomag.services.UserService;
import com.krasimirkolchev.photomag.web.annotations.PageTitle;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    private final UserService userService;

    public HomeController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    @PageTitle("Home")
    public String index() {

        return "home";
    }

    @GetMapping("/home")
    public String home() {

        return "home";
    }

    
}
