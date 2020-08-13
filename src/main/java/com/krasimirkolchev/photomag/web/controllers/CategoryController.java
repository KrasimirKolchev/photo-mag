package com.krasimirkolchev.photomag.web.controllers;

import com.krasimirkolchev.photomag.models.bindingModels.CategoryAddBindingModel;
import com.krasimirkolchev.photomag.models.serviceModels.ProductCategoryServiceModel;
import com.krasimirkolchev.photomag.services.ProductCategoryService;
import com.krasimirkolchev.photomag.validation.CategoryValidation;
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

import java.io.IOException;

@Controller
@RequestMapping("/categories")
public class CategoryController {
    private final ProductCategoryService productCategoryService;
    private final CategoryValidation categoryValidation;
    private final ModelMapper modelMapper;

    @Autowired
    public CategoryController(ProductCategoryService productCategoryService, CategoryValidation categoryValidation,
                              ModelMapper modelMapper) {
        this.productCategoryService = productCategoryService;
        this.categoryValidation = categoryValidation;
        this.modelMapper = modelMapper;
    }

    @GetMapping()
    @PageTitle("Categories")
    @PreAuthorize("isAuthenticated()")
    public String allCategories(Model model) {
        if (!model.containsAttribute("categories")) {
            model.addAttribute("categories", this.productCategoryService.getAllCategories());
        }
        return "categories";
    }

    @GetMapping("/add")
    @PreAuthorize("hasAnyRole('ROOT_ADMIN, ADMIN')")
    @PageTitle("Add Product category")
    public String addCategory(Model model) {
        if (!model.containsAttribute("categoryAddBindingModel")) {
            model.addAttribute("categoryAddBindingModel", new CategoryAddBindingModel());
        }
        return "add-category";
    }

    @PostMapping("/add")
    @PreAuthorize("hasAnyRole('ROOT_ADMIN, ADMIN')")
    public String addCategoryConf(@ModelAttribute("categoryAddBindingModel")
                  CategoryAddBindingModel categoryAddBindingModel, BindingResult result, RedirectAttributes attributes) throws IOException {

        this.categoryValidation.validate(categoryAddBindingModel, result);

        if (result.hasErrors()) {
            attributes.addFlashAttribute("categoryAddBindingModel", categoryAddBindingModel);
            attributes.addFlashAttribute("org.springframework.validation.BindingResult.categoryAddBindingModel"
                    , result);
            return "redirect:/categories/add";
        }

        ProductCategoryServiceModel categoryServiceModel = this.productCategoryService.addCategory(this
                .modelMapper.map(categoryAddBindingModel, ProductCategoryServiceModel.class), categoryAddBindingModel.getPhoto());
        return "redirect:/";
    }

}
