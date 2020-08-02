package com.krasimirkolchev.photomag.web.controllers;

import com.krasimirkolchev.photomag.models.bindingModels.BrandAddBindingModel;
import com.krasimirkolchev.photomag.models.serviceModels.BrandServiceModel;
import com.krasimirkolchev.photomag.services.BrandService;
import com.krasimirkolchev.photomag.validation.BrandAddValidation;
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

import javax.validation.Valid;

@Controller
@RequestMapping("/brands")
public class BrandController {
    private final BrandService brandService;
    private final BrandAddValidation brandAddValidation;
    private final ModelMapper modelMapper;

    @Autowired
    public BrandController(BrandService brandService, BrandAddValidation brandAddValidation, ModelMapper modelMapper) {
        this.brandService = brandService;
        this.brandAddValidation = brandAddValidation;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/add")
    @PreAuthorize("hasAnyRole('ROLE_ROOT_ADMIN, ROLE_ADMIN')")
    @PageTitle("Add brand")
    public String addBrand(Model model) {
        if (!model.containsAttribute("brandAddBindingModel")) {
            model.addAttribute("brandAddBindingModel", new BrandAddBindingModel());
        }
        return "add-brand";
    }

    @PostMapping("/add")
    @PreAuthorize("hasAnyRole('ROLE_ROOT_ADMIN, ROLE_ADMIN')")
    public String addBrandConf(@ModelAttribute("brandAddBindingModel") BrandAddBindingModel brandAddBindingModel,
                               BindingResult result, RedirectAttributes attributes) {

        this.brandAddValidation.validate(brandAddBindingModel, result);

        if (result.hasErrors()) {
            attributes.addFlashAttribute("brandAddBindingModel", brandAddBindingModel);
            attributes.addFlashAttribute("org.springframework.validation.BindingResult.brandAddBindingModel"
                    , result);
            return "redirect:/brands/add";
        }

        BrandServiceModel brandServiceModel = this.brandService.createBrand(this.modelMapper
                .map(brandAddBindingModel, BrandServiceModel.class));
        return "redirect:/home";
    }

}
