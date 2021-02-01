package com.krasimirkolchev.photomag.web.controllers;

import com.krasimirkolchev.photomag.models.bindingModels.AddressGetBindingModel;
import com.krasimirkolchev.photomag.models.bindingModels.ExpOrdersDatesBindingModel;
import com.krasimirkolchev.photomag.models.serviceModels.UserServiceModel;
import com.krasimirkolchev.photomag.services.OrderService;
import com.krasimirkolchev.photomag.services.UserService;
import com.krasimirkolchev.photomag.validation.ExportOrdersValidation;
import com.krasimirkolchev.photomag.web.annotations.PageTitle;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.security.Principal;

@Controller
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;
    private final UserService userService;
    private final ModelMapper modelMapper;
    private final ExportOrdersValidation exportOrdersValidation;

    @Autowired
    public OrderController(OrderService orderService, UserService userService, ModelMapper modelMapper
            , ExportOrdersValidation exportOrdersValidation) {
        this.orderService = orderService;
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.exportOrdersValidation = exportOrdersValidation;
    }

    @GetMapping("/all")
    @PageTitle("All orders")
    @PreAuthorize("isAuthenticated()")
    public String getAllOrders(Model model, Principal principal) {
        if (!model.containsAttribute("orders")) {
            UserServiceModel userServiceModel = this.modelMapper
                    .map(this.userService.getUserByUsername(principal.getName()), UserServiceModel.class);

            if (userServiceModel.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
                model.addAttribute("orders", this.orderService.getAllOrders());
            } else {
                model.addAttribute("orders", this.orderService
                        .getAllOrdersByUsername(userServiceModel.getUsername()));
            }
        }

        return "orders-all";
    }

    @GetMapping("/add-details")
    @PageTitle("Add order details")
    @PreAuthorize("isAuthenticated()")
    public String addOrderDetails(Model model, Principal principal) {
        if (!model.containsAttribute("addressGetBindingModel")) {
            model.addAttribute("addressGetBindingModel", new AddressGetBindingModel());
        }
        model.addAttribute("addresses", this.userService
                .getUserByUsername(principal.getName()).getAddresses());
        return "add-order-details";
    }

    @PostMapping("/add-details")
    @PreAuthorize("isAuthenticated()")
    public String addOrderDetailsConf(@ModelAttribute("addressGetBindingModel") AddressGetBindingModel addressGetBindingModel,
                                      BindingResult result, RedirectAttributes attributes, Model model) {

        if (result.hasErrors()) {
            attributes.addFlashAttribute("addressGetBindingModel", addressGetBindingModel);
            attributes.addFlashAttribute("org.springframework.validation.BindingResult.addressGetBindingModel"
                    , result);
            return "redirect:/orders/add-details";
        }

        model.addAttribute("addressGetBindingModel", addressGetBindingModel);
        return "redirect:/checkout";
    }

    @GetMapping("/export")
    @PreAuthorize("hasAnyRole('ROOT_ADMIN', 'ADMIN')")
    public String exportOrders(Model model) {
        if (!model.containsAttribute("expOrdersDatesBindingModel")) {
            model.addAttribute("expOrdersDatesBindingModel", new ExpOrdersDatesBindingModel());
        }

        return "export-orders";
    }

    @PostMapping("/export")
    @PreAuthorize("hasAnyRole('ROOT_ADMIN', 'ADMIN')")
    public String exportOrders(@ModelAttribute("expOrdersDatesBindingModel") ExpOrdersDatesBindingModel expOrdersDatesBindingModel,
                               BindingResult result, RedirectAttributes attributes) {

        this.exportOrdersValidation.validate(expOrdersDatesBindingModel, result);

        if (result.hasErrors()) {
            attributes.addFlashAttribute("expOrdersDatesBindingModel", expOrdersDatesBindingModel);
            attributes.addFlashAttribute("org.springframework.validation.BindingResult.expOrdersDatesBindingModel"
                    , result);
            return "redirect:/orders/export";
        }


        if (this.orderService.hasOrdersForPeriod(expOrdersDatesBindingModel)) {

            attributes.addFlashAttribute("NotFound", "There are no orders for selected period!");
            attributes.addFlashAttribute("expOrdersDatesBindingModel", expOrdersDatesBindingModel);
            attributes.addFlashAttribute("org.springframework.validation.BindingResult.expOrdersDatesBindingModel"
                    , result);

            return "redirect:/orders/export";
        }

        attributes.addFlashAttribute("expOrdersDatesBindingModel", expOrdersDatesBindingModel);

        return "redirect:/orders/orderexport";
    }

    @GetMapping(value = "/orderexport")
    @PreAuthorize("hasAnyRole('ROOT_ADMIN', 'ADMIN')")
    public ResponseEntity<FileSystemResource> getAttachment(@ModelAttribute("expOrdersDates")
                    ExpOrdersDatesBindingModel expOrdersDatesBindingModel) throws IOException {

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Description", "File Transfer");
        headers.add("Content-Disposition", "attachment; filename=exportedOrders.xlsx");
        headers.add("Content-Transfer-Encoding", "binary");
        headers.add("Connection", "Keep-Alive");

        File file = this.orderService.exportOrdersForPeriod(expOrdersDatesBindingModel);

        MediaType mediaType = MediaTypeFactory
                .getMediaType(new FileSystemResource(file))
                .orElse(MediaType.APPLICATION_OCTET_STREAM);

        headers.setContentType(mediaType);

        return ResponseEntity.ok().headers(headers).body(new FileSystemResource(file));
    }

}
