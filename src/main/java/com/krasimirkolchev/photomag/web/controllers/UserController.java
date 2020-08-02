package com.krasimirkolchev.photomag.web.controllers;

import com.krasimirkolchev.photomag.models.bindingModels.UserEditBindingModel;
import com.krasimirkolchev.photomag.models.bindingModels.UserLoginBindingModel;
import com.krasimirkolchev.photomag.models.bindingModels.UserRegBindingModel;
import com.krasimirkolchev.photomag.models.serviceModels.UserServiceModel;
import com.krasimirkolchev.photomag.services.UserService;
import com.krasimirkolchev.photomag.validation.UserEditValidation;
import com.krasimirkolchev.photomag.validation.UserRegisterValidation;
import com.krasimirkolchev.photomag.web.annotations.PageTitle;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;

@Controller
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final UserRegisterValidation userRegisterValidation;
    private final UserEditValidation userEditValidation;
    private final ModelMapper modelMapper;

    @Autowired
    public UserController(UserService userService, UserRegisterValidation userRegisterValidation,
                          UserEditValidation userEditValidation, ModelMapper modelMapper) {
        this.userService = userService;
        this.userRegisterValidation = userRegisterValidation;
        this.userEditValidation = userEditValidation;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/register")
    @PageTitle("Register")
    public String userRegister(Model model) {
        if (!model.containsAttribute("userRegBindingModel")) {
            model.addAttribute("userRegBindingModel", new UserRegBindingModel());
        }
        return "register";
    }

    @PostMapping("/register")
    public String userRegisterConf(@ModelAttribute("userRegBindingModel") UserRegBindingModel userRegBindingModel
            , BindingResult result, RedirectAttributes attributes) throws IOException {

        this.userRegisterValidation.validate(userRegBindingModel, result);

        if (result.hasErrors()) {
            attributes.addFlashAttribute("userRegBindingModel", userRegBindingModel);
            attributes.addFlashAttribute("org.springframework.validation.BindingResult.userRegBindingModel"
                    , result);
            return "redirect:register";
        }

        UserServiceModel userServiceModel = this.userService.registerUser(this.modelMapper
                .map(userRegBindingModel, UserServiceModel.class), userRegBindingModel.getFile());

        return "redirect:/login";
    }

    @GetMapping("/login")
    @PageTitle("Login")
    public String userLogin(Model model) {
        if (!model.containsAttribute("userLoginBindingModel")) {
            model.addAttribute("userLoginBindingModel", new UserLoginBindingModel());
        }
        return "login";
    }

    @GetMapping("/profile")
    @PageTitle("Profile")
    @PreAuthorize("isAuthenticated()")
    public String userProfile(Model model, Principal principal) {
        if (!model.containsAttribute("user")) {
            model.addAttribute("user", this.userService.getUserByUsername(principal.getName()));
        }
        return "profile";
    }

    @GetMapping("/edit")
    @PageTitle("Edit User")
    @PreAuthorize("isAuthenticated()")
    public String editUser(Model model, Principal principal) {
        if (!model.containsAttribute("userEditBindingModel")) {
            UserServiceModel userServiceModel = this.modelMapper
                    .map(this.userService.getUserByUsername(principal.getName()), UserServiceModel.class);
            UserEditBindingModel userEditBindingModel = this.modelMapper.map(userServiceModel, UserEditBindingModel.class);
            userEditBindingModel.setPassword(null);
            model.addAttribute("userEditBindingModel", userEditBindingModel);
        }
        return "edit-user";
    }

    @PostMapping("/edit")
    @PreAuthorize("isAuthenticated()")
    public String editUserConf(@ModelAttribute("userEditBindingModel") UserEditBindingModel userEditBindingModel
            , BindingResult result, RedirectAttributes attributes, Principal principal) {

        this.userEditValidation.validate(userEditBindingModel, result);

        if (result.hasErrors()) {
            userEditBindingModel.setPassword(null);
            attributes.addFlashAttribute("userEditBindingModel", userEditBindingModel);
            attributes.addFlashAttribute("org.springframework.validation.BindingResult.userEditBindingModel"
                    , result);
            return "redirect:/users/edit";
        }

        UserServiceModel userServiceModel = this.userService.editUser(this.modelMapper
                .map(userEditBindingModel, UserServiceModel.class), userEditBindingModel.getOldPassword(), principal.getName());

        return "redirect:/users/profile";
    }
}
