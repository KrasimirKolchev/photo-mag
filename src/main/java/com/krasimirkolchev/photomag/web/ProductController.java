package com.krasimirkolchev.photomag.web;

import com.krasimirkolchev.photomag.models.bindingModels.CartItemAddBindModel;
import com.krasimirkolchev.photomag.models.bindingModels.ProductAddBindingModel;
import com.krasimirkolchev.photomag.models.entities.ProductCategory;
import com.krasimirkolchev.photomag.models.serviceModels.ProductServiceModel;
import com.krasimirkolchev.photomag.services.ProductCategoryService;
import com.krasimirkolchev.photomag.services.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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
    public String addProduct(Model model) {
        if (!model.containsAttribute("productAddBindingModel")) {
            model.addAttribute("productAddBindingModel", new ProductAddBindingModel());
            model.addAttribute("productCategory", this.productCategoryService.getAllCategories());
        }
        return "add-product";
    }

    @PostMapping("/add")
    public String addProductConf(@Valid @ModelAttribute("productAddBindingModel") ProductAddBindingModel productAddBindingModel,
                                 BindingResult result, RedirectAttributes attributes) {
        if (result.hasErrors()) {
            attributes.addFlashAttribute("productAddBindingModel", productAddBindingModel);
            attributes.addFlashAttribute("");
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
            return "redirect:/products/add";
        }

    }

    @GetMapping("/categories")
    public String allCategories(Model model) {
        if (!model.containsAttribute("categories")) {
            model.addAttribute("categories", this.productCategoryService.getAllCategories());
        }
        return "categories";
    }

    @GetMapping("/{categoryName}")
    public String allProductsByCategory(@PathVariable(name = "categoryName") String categoryName, Model model) {
        if (!model.containsAttribute("products")) {
            model.addAttribute("products", this.productService.getProductsByCategoryName(categoryName.replace('+', ' ')));
        }
        return "products";
    }

    @GetMapping("/details/{id}")
    public String allCategories(@PathVariable(name = "id") String id, Model model) {
        if (!model.containsAttribute("product")) {
            model.addAttribute("product", this.productService.getProductById(id));
            model.addAttribute("cartItem", new CartItemAddBindModel());
        }
        return "product-details";
    }
}
