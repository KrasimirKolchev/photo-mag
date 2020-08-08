package com.krasimirkolchev.photomag.web.controllers;

import com.krasimirkolchev.photomag.models.bindingModels.AddressAddBindingModel;
import com.krasimirkolchev.photomag.models.serviceModels.AddressServiceModel;
import com.krasimirkolchev.photomag.models.serviceModels.UserServiceModel;
import com.krasimirkolchev.photomag.services.AddressService;
import com.krasimirkolchev.photomag.services.UserService;
import com.krasimirkolchev.photomag.validation.AddressValidation;
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

import java.security.Principal;

@Controller
@RequestMapping("/addresses")
public class AddressController {
    private final AddressService addressService;
    private final UserService userService;
    private final AddressValidation addressValidation;
    private final ModelMapper modelMapper;

    @Autowired
    public AddressController(AddressService addressService, UserService userService, AddressValidation addressValidation,
                             ModelMapper modelMapper) {
        this.addressService = addressService;
        this.userService = userService;
        this.addressValidation = addressValidation;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/add")
    @PageTitle("Add address")
    @PreAuthorize("isAuthenticated()")
    public String addAddress(Model model) {
        if (!model.containsAttribute("addressAddBindingModel")) {
            model.addAttribute("addressAddBindingModel", new AddressAddBindingModel());
        }
        return "add-address";
    }

    @PostMapping("/add")
    @PreAuthorize("isAuthenticated()")
    public String addAddressConf(@ModelAttribute("addressAddBindingModel") AddressAddBindingModel addressAddBindingModel,
                                 BindingResult result, RedirectAttributes attributes, Principal principal) {

        this.addressValidation.validate(addressAddBindingModel, result);

        if (result.hasErrors()) {
            attributes.addFlashAttribute("addressAddBindingModel", addressAddBindingModel);
            attributes.addFlashAttribute("org.springframework.validation.BindingResult.addressAddBindingModel"
                    , result);
            return "redirect:/addresses/add";
        }

        UserServiceModel userServiceModel = this.userService
                .addAddressToUser(this.modelMapper.map(addressAddBindingModel, AddressServiceModel.class), principal.getName());

        return "redirect:/users/profile";
    }
}
