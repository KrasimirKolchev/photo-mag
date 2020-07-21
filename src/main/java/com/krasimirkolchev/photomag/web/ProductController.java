package com.krasimirkolchev.photomag.web;

import com.krasimirkolchev.photomag.models.bindingModels.ProductAddBindingModel;
import com.krasimirkolchev.photomag.models.entities.enums.ProductCategory;
import com.krasimirkolchev.photomag.models.serviceModels.ProductServiceModel;
import com.krasimirkolchev.photomag.services.ProductService;
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
import java.io.IOException;

@Controller
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;
    private final ModelMapper modelMapper;

    @Autowired
    public ProductController(ProductService productService,  ModelMapper modelMapper) {
        this.productService = productService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/add")
    public String addProduct(Model model) {
        if (!model.containsAttribute("productAddBindingModel")) {
            model.addAttribute("productAddBindingModel", new ProductAddBindingModel());
            model.addAttribute("productCategory", ProductCategory.values());
        }
        return "add-product";
    }

    @PostMapping("/add")
    public String addProductConf(@Valid @ModelAttribute("productAddBindingModel") ProductAddBindingModel productAddBindingModel,
                                 BindingResult result, RedirectAttributes attributes) throws IOException {
        if (result.hasErrors()) {
            attributes.addFlashAttribute("productAddBindingModel", productAddBindingModel);
            attributes.addFlashAttribute("");
        }

        ProductServiceModel productServiceModel = this.productService.addProduct(this.modelMapper
                .map(productAddBindingModel, ProductServiceModel.class), productAddBindingModel.getProductGallery());
        return "redirect:/";
    }

    @GetMapping("/allCategories")
    public String allProdCategories(Model model) {
        if (!model.containsAttribute("categories")) {
            model.addAttribute("categories", ProductCategory.values());
        }
        return "product-categories";
    }
}
