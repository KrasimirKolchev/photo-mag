package com.krasimirkolchev.photomag.web.controllers;

import com.krasimirkolchev.photomag.models.bindingModels.CategoryAddBindingModel;
import com.krasimirkolchev.photomag.models.entities.ProductCategory;
import com.krasimirkolchev.photomag.services.ProductCategoryService;
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
import java.io.IOException;

@Controller
@RequestMapping("/categories")
public class CategoryController {
    private final ProductCategoryService productCategoryService;
    private final ModelMapper modelMapper;

    @Autowired
    public CategoryController(ProductCategoryService productCategoryService, ModelMapper modelMapper) {
        this.productCategoryService = productCategoryService;
        this.modelMapper = modelMapper;
    }

    @GetMapping()
    @PageTitle("Categories")
    public String allCategories(Model model) {
        if (!model.containsAttribute("categories")) {
            model.addAttribute("categories", this.productCategoryService.getAllCategories());
        }
        return "categories";
    }

    @GetMapping("/add")
    @PreAuthorize("hasAnyRole('ROLE_ROOT_ADMIN, ROLE_ADMIN')")
    @PageTitle("Add Product category")
    public String addCategory(Model model) {
        if (!model.containsAttribute("categoryAddBindingModel")) {
            model.addAttribute("categoryAddBindingModel", new CategoryAddBindingModel());
        }
        return "add-category";
    }

    @PostMapping("/add")
    @PreAuthorize("hasAnyRole('ROLE_ROOT_ADMIN, ROLE_ADMIN')")
    public String addCategoryConf(@Valid @ModelAttribute("categoryAddBindingModel")
                  CategoryAddBindingModel categoryAddBindingModel, BindingResult result, RedirectAttributes attributes) throws IOException {

        if (result.hasErrors()) {
            attributes.addFlashAttribute("categoryAddBindingModel", categoryAddBindingModel);
            return "redirect:/categories/add";
        }

        this.productCategoryService.addCategory(categoryAddBindingModel);
        return "redirect:/";
    }

}
