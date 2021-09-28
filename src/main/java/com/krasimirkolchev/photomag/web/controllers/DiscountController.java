package com.krasimirkolchev.photomag.web.controllers;

import com.krasimirkolchev.photomag.models.bindingModels.VoucherAddBindingModel;
import com.krasimirkolchev.photomag.models.serviceModels.BrandServiceModel;
import com.krasimirkolchev.photomag.models.serviceModels.ProductCategoryServiceModel;
import com.krasimirkolchev.photomag.models.serviceModels.VoucherServiceModel;
import com.krasimirkolchev.photomag.services.BrandService;
import com.krasimirkolchev.photomag.services.DiscountService;
import com.krasimirkolchev.photomag.services.ProductCategoryService;
import com.krasimirkolchev.photomag.validation.VoucherAddValidation;
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

import javax.mail.MessagingException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/discounts")
public class DiscountController {
    private final DiscountService discountService;
    private final ModelMapper modelMapper;
    private final ProductCategoryService productCategoryService;
    private final BrandService brandService;
    private final VoucherAddValidation voucherAddValidation;

    @Autowired
    public DiscountController(DiscountService discountService, ModelMapper modelMapper,
                              ProductCategoryService productCategoryService, BrandService brandService,
                              VoucherAddValidation voucherAddValidation) {
        this.discountService = discountService;
        this.modelMapper = modelMapper;
        this.productCategoryService = productCategoryService;
        this.brandService = brandService;
        this.voucherAddValidation = voucherAddValidation;
    }

    @GetMapping("/voucher/create")
    @PreAuthorize("hasAnyRole('ROOT_ADMIN, ADMIN')")
    @PageTitle("Create Voucher")
    public String createVoucher(Model model) {
        if (!model.containsAttribute("voucherAddBindingModel")) {
            model.addAttribute("voucherAddBindingModel", new VoucherAddBindingModel());
        }
        model.addAttribute("productCategories", this.productCategoryService.getAllCategories());
        model.addAttribute("productBrands", this.brandService.getAllBrands());

        return "voucher";
    }

    @PostMapping("/voucher/create")
    @PreAuthorize("hasAnyRole('ROOT_ADMIN, ADMIN')")
    public String createVoucherConf(@ModelAttribute("voucherAddBindingModel") VoucherAddBindingModel voucherAddBindingModel,
                                    BindingResult result, RedirectAttributes attributes) throws MessagingException {

        this.voucherAddValidation.validate(voucherAddBindingModel, result);

        if (result.hasErrors()) {
            attributes.addFlashAttribute("voucherAddBindingModel", voucherAddBindingModel);
            attributes.addFlashAttribute("org.springframework.validation.BindingResult.voucherAddBindingModel"
                    , result);
            return "redirect:/discounts/voucher/create";
        }

        VoucherServiceModel voucherServiceModel = this.modelMapper.map(voucherAddBindingModel, VoucherServiceModel.class);
        List<BrandServiceModel> brands = voucherAddBindingModel.getBrands().stream()
                .map(this.brandService::getBrandById)
                .collect(Collectors.toList());
        voucherServiceModel.setBrands(brands);
        List<ProductCategoryServiceModel> categories = voucherAddBindingModel.getCategories().stream()
                .map(this.productCategoryService::getCategoryById)
                .collect(Collectors.toList());
        voucherServiceModel.setCategories(categories);

        voucherServiceModel.setStartDate(LocalDateTime
                .parse(voucherAddBindingModel.getStartDate() + "T00:00:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        voucherServiceModel.setEndDate(LocalDateTime
                .parse(voucherAddBindingModel.getEndDate() + "T23:59:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME));

        this.discountService.createVoucher(voucherServiceModel);

        return "redirect:/";
    }

}
