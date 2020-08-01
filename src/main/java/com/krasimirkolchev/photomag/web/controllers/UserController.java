package com.krasimirkolchev.photomag.web.controllers;

import com.krasimirkolchev.photomag.models.bindingModels.UserEditBindingModel;
import com.krasimirkolchev.photomag.models.bindingModels.UserLoginBindingModel;
import com.krasimirkolchev.photomag.models.bindingModels.UserRegBindingModel;
import com.krasimirkolchev.photomag.models.serviceModels.UserServiceModel;
import com.krasimirkolchev.photomag.services.AddressService;
import com.krasimirkolchev.photomag.services.UserService;
import com.krasimirkolchev.photomag.web.annotations.PageTitle;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.security.Principal;

@Controller
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final ModelMapper modelMapper;

    @Autowired
    public UserController(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
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
    public String userRegisterConf(@Valid @ModelAttribute("userRegBindingModel") UserRegBindingModel userRegBindingModel
            , BindingResult result, RedirectAttributes attributes) {

        if (!userRegBindingModel.getPassword().equals(userRegBindingModel.getConfirmPassword())) {
            result.rejectValue("password", "error.userRegBindingModel", "Passwords doesn't match");
        }

        if (result.hasErrors()) {
            attributes.addFlashAttribute("userRegBindingModel", userRegBindingModel);
            attributes.addFlashAttribute("org.springframework.validation.BindingResult.userRegBindingModel"
                    , result);
            return "redirect:register";
        }

        try {
            UserServiceModel userServiceModel = this.userService.registerUser(this.modelMapper
                    .map(userRegBindingModel, UserServiceModel.class), userRegBindingModel.getFile());

            return "redirect:/login";
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            attributes.addFlashAttribute("userRegBindingModel", userRegBindingModel);
            attributes.addFlashAttribute("org.springframework.validation.BindingResult.userRegBindingModel"
                    , result);
            return "redirect:register";
        }

    }

    @GetMapping("/login")
    @PageTitle("Login")
    public String userLogin(Model model) {
        if (!model.containsAttribute("userLoginBindingModel")) {
            model.addAttribute("userLoginBindingModel", new UserLoginBindingModel());
        }
        return "login";
    }

    @GetMapping("/gallery")
    public String userGallery() {
        return null;
    }

    @GetMapping("/gallery/addPhoto")
    public String userGalleryAdd() {
        return null;
    }

    @PostMapping("/gallery/addPhoto")
    public String useGalleryAddConf(@RequestParam(name = "file", required = false) MultipartFile file, Principal principal
            , RedirectAttributes redirectAttr) {

        try {
            this.userService.addPhotoToUserGallery(principal, file);

            return "home";
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
//            redirectAttr.addFlashAttribute("userRegBindingModel", userRegBindingModel);
//            redirectAttr.addFlashAttribute("org.springframework.validation.BindingResult.userRegBindingModel"
//                    , bindingResult);
            return "redirect:register";
        }

    }

    @GetMapping("/profile")
    @PageTitle("Profile")
    public String userProfile(Model model, Principal principal) {
        if (!model.containsAttribute("user")) {
            model.addAttribute("user", this.userService.getUserByUsername(principal.getName()));
        }
        return "profile";
    }

    @GetMapping("/edit")
    @PageTitle("Edit User")
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
    public String editUserConf(@Valid @ModelAttribute("userEditBindingModel") UserEditBindingModel userEditBindingModel
            , BindingResult result, RedirectAttributes attributes, Principal principal) {

        if (!userEditBindingModel.getPassword().equals(userEditBindingModel.getConfirmPassword())) {
            result.rejectValue("password", "error.userEditBindingModel", "Passwords doesn't match");
        }

        if (result.hasErrors()) {
            userEditBindingModel.setPassword(null);
            attributes.addFlashAttribute("userEditBindingModel", userEditBindingModel);
            attributes.addFlashAttribute("org.springframework.validation.BindingResult.userEditBindingModel"
                    , result);
            return "redirect:edit-user";
        }

        try {
            UserServiceModel userServiceModel = this.userService.editUser(this.modelMapper
                    .map(userEditBindingModel, UserServiceModel.class), userEditBindingModel.getOldPassword(), principal.getName());

            return "redirect:/users/profile";
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            attributes.addFlashAttribute("userEditBindingModel", userEditBindingModel);
            attributes.addFlashAttribute("org.springframework.validation.BindingResult.userEditBindingModel"
                    , result);
            return "redirect:edit-user";
        }

    }
}
