package com.krasimirkolchev.photomag.web.controllers;

import com.krasimirkolchev.photomag.models.bindingModels.AddressAddBindingModel;
import com.krasimirkolchev.photomag.models.serviceModels.UserServiceModel;
import com.krasimirkolchev.photomag.services.AddressService;
import com.krasimirkolchev.photomag.services.UserService;
import com.krasimirkolchev.photomag.web.annotations.PageTitle;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.security.Principal;

@Controller
@RequestMapping("/addresses")
public class AddressController {
    private final AddressService addressService;
    private final UserService userService;
    private final ModelMapper modelMapper;

    @Autowired
    public AddressController(AddressService addressService, UserService userService, ModelMapper modelMapper) {
        this.addressService = addressService;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/add")
    @PageTitle("Add address")
    public String addAddress(Model model) {
        if (!model.containsAttribute("addressAddBindingModel")) {
            model.addAttribute("addressAddBindingModel", new AddressAddBindingModel());
        }
        return "add-address";
    }

    @PostMapping("/add")
    public String addAddressConf(@Valid @ModelAttribute("addressAddBindingModel") AddressAddBindingModel addressAddBindingModel,
                                 BindingResult result, RedirectAttributes attributes, Principal principal) {

        if (result.hasErrors()) {
            attributes.addFlashAttribute("addressAddBindingModel", addressAddBindingModel);
        }

        UserServiceModel userServiceModel = this.userService.addAddressToUser(addressAddBindingModel, principal);

        return "redirect:/users/profile";
    }
}
