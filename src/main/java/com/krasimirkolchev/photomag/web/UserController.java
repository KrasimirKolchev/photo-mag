package com.krasimirkolchev.photomag.web;

import com.krasimirkolchev.photomag.models.bondingModels.UserLoginBindingModel;
import com.krasimirkolchev.photomag.models.bondingModels.UserRegBindingModel;
import com.krasimirkolchev.photomag.models.entities.User;
import com.krasimirkolchev.photomag.services.UserService;
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
    public String userRegister(Model model) {
        if (!model.containsAttribute("userRegBindingModel")) {
            model.addAttribute("userRegBindingModel", new UserRegBindingModel());
        }
        return "register";
    }

    @PostMapping("/register")
    public String userRegisterConf(@Valid @ModelAttribute("userRegBindingModel") UserRegBindingModel userRegBindingModel
            , BindingResult bindingResult, RedirectAttributes redirectAttr
            , @RequestParam(name = "file", required = false) MultipartFile file) {

        System.out.println();
        if (!userRegBindingModel.getPassword().equals(userRegBindingModel.getConfirmPassword())) {
            bindingResult.rejectValue("password", "error.userRegBindingModel", "psw not match!");
        }

        if (bindingResult.hasErrors()) {
            redirectAttr.addFlashAttribute("userRegBindingModel", userRegBindingModel);
            redirectAttr.addFlashAttribute("org.springframework.validation.BindingResult.userRegBindingModel"
                    , bindingResult);
            return "redirect:register";
        }

        try {
            User user = this.userService.registerUser(this.modelMapper.map(userRegBindingModel, User.class), file);

            return "home";
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
            redirectAttr.addFlashAttribute("userRegBindingModel", userRegBindingModel);
            redirectAttr.addFlashAttribute("org.springframework.validation.BindingResult.userRegBindingModel"
                    , bindingResult);
            return "redirect:register";
        }

    }

    @GetMapping("/login")
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
    public String useGalleryAdd() {
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

}
