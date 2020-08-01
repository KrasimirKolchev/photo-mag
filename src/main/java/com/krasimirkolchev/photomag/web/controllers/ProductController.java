package com.krasimirkolchev.photomag.web.controllers;

import com.krasimirkolchev.photomag.models.bindingModels.CartItemAddBindModel;
import com.krasimirkolchev.photomag.models.bindingModels.ProductAddBindingModel;
import com.krasimirkolchev.photomag.models.bindingModels.ProductEditBindingModel;
import com.krasimirkolchev.photomag.models.entities.ProductCategory;
import com.krasimirkolchev.photomag.models.serviceModels.ProductServiceModel;
import com.krasimirkolchev.photomag.services.ProductCategoryService;
import com.krasimirkolchev.photomag.services.ProductService;
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

@Controller
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;
    private final ProductCategoryService productCategoryService;
    private final ModelMapper modelMapper;

    @Autowired
    public ProductController(ProductService productService, ProductCategoryService productCategoryService, ModelMapper modelMapper) {
        this.productService = productService;
        this.productCategoryService = productCategoryService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/add")
    @PreAuthorize("hasAnyRole('ROLE_ROOT_ADMIN, ROLE_ADMIN')")
    @PageTitle("Add product")
    public String addProduct(Model model) {
        if (!model.containsAttribute("productAddBindingModel")) {
            model.addAttribute("productAddBindingModel", new ProductAddBindingModel());
            model.addAttribute("productCategory", this.productCategoryService.getAllCategories());
        }
        return "add-product";
    }

    @PostMapping("/add")
    @PreAuthorize("hasAnyRole('ROLE_ROOT_ADMIN, ROLE_ADMIN')")
    public String addProductConf(@Valid @ModelAttribute("productAddBindingModel") ProductAddBindingModel productAddBindingModel,
                                 BindingResult result, RedirectAttributes attributes) {
        System.out.println();
        if (result.hasErrors()) {
            attributes.addFlashAttribute("productAddBindingModel", productAddBindingModel);
            attributes.addFlashAttribute("org.springframework.validation.BindingResult.productAddBindingModel"
                    , result);
            return "redirect:/products/add";
        }

        try {
            ProductServiceModel productServiceModel = this.modelMapper.map(productAddBindingModel, ProductServiceModel.class);
            ProductCategory category = this.productCategoryService
                    .getCategoryById(productAddBindingModel.getProductCategory());

            productServiceModel.setProductCategory(category);
            this.productService.addProduct(productServiceModel, productAddBindingModel.getProductGallery());
            return "redirect:/";
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            attributes.addFlashAttribute("productAddBindingModel", productAddBindingModel);
            return "redirect:/products/add";
        }

    }

    @GetMapping("/{categoryName}")
    @PageTitle("Products")
    public String allProductsByCategory(@PathVariable(name = "categoryName") String categoryName, Model model) {
        if (!model.containsAttribute("products")) {
            model.addAttribute("products", this.productService.getProductsByCategoryName(categoryName.replace('+', ' ')));
        }
        return "products";
    }

    @GetMapping("/details/{id}")
    @PageTitle("Product details")
    public String allCategories(@PathVariable(name = "id") String id, Model model) {
        if (!model.containsAttribute("product")) {
            model.addAttribute("product", this.productService.getProductById(id));
            model.addAttribute("cartItem", new CartItemAddBindModel());
        }
        return "product-details";
    }

    @GetMapping("/edit{id}")
    @PreAuthorize("hasAnyRole('ROLE_ROOT_ADMIN, ROLE_ADMIN')")
    @PageTitle("Edit Product")
    public String editProduct(@PathVariable(name = "id") String id, Model model) {
        if (!model.containsAttribute("productEditBindingModel")) {
            model.addAttribute("id", id);
            model.addAttribute("productEditBindingModel", this.modelMapper
                    .map(this.productService.getProductById(id), ProductEditBindingModel.class));
        }
        return "edit-product";
    }

    @PostMapping("/edit{id}")
    @PreAuthorize("hasAnyRole('ROLE_ROOT_ADMIN, ROLE_ADMIN')")
    public String editProductConf(@Valid @ModelAttribute("productEditBindingModel") ProductEditBindingModel productEditBindingModel,
                                  BindingResult result, RedirectAttributes attributes, @PathVariable("id") String id) {

        if (result.hasErrors()) {
            attributes.addFlashAttribute("productEditBindingModel", productEditBindingModel);
            attributes.addFlashAttribute("org.springframework.validation.BindingResult.productEditBindingModel"
                    , result);
            return "redirect:/products/edit" + productEditBindingModel.getId();
        }

        ProductServiceModel productServiceModel = this.productService.editProduct(id, this.modelMapper
                .map(productEditBindingModel, ProductServiceModel.class));

        return "redirect:/products/details/" + productServiceModel.getId();
    }

}
