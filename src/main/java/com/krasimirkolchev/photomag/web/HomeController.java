package com.krasimirkolchev.photomag.web;

import com.krasimirkolchev.photomag.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    private final UserService userService;
    private final ImageUtil imageUtil;

    public HomeController(UserService userService, ImageUtil imageUtil) {
        this.userService = userService;
        this.imageUtil = imageUtil;
    }

    @GetMapping("/")
    public String home() {

        return "index";
    }

    
}
