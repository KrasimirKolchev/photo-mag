package com.krasimirkolchev.photomag.web;

import com.krasimirkolchev.photomag.models.bindingModels.CategoryAddBindingModel;
import com.krasimirkolchev.photomag.models.entities.ProductCategory;
import com.krasimirkolchev.photomag.services.ProductCategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;

@Controller
public class CategoryController {
    private final ProductCategoryService productCategoryService;
    private final ModelMapper modelMapper;

    @Autowired
    public CategoryController(ProductCategoryService productCategoryService, ModelMapper modelMapper) {
        this.productCategoryService = productCategoryService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/categories/add")
    public String addCategory(Model model) {
        if (!model.containsAttribute("categoryAddBindingModel")) {
            model.addAttribute("categoryAddBindingModel", new CategoryAddBindingModel());
        }
        return "create-category";
    }

    @PostMapping("/categories/add")
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
