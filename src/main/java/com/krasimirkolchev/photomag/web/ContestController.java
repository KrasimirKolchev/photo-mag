package com.krasimirkolchev.photomag.web;

import com.krasimirkolchev.photomag.models.bindingModels.ContestCreateBindingModel;
import com.krasimirkolchev.photomag.repositories.ContestRepository;
import com.krasimirkolchev.photomag.services.ContestService;
import com.krasimirkolchev.photomag.services.impl.CloudinaryServiceImpl;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/contests")
public class ContestController {
    private final ContestService contestService;
    private final ModelMapper modelMapper;

    @Autowired
    public ContestController(ContestService contestService, ModelMapper modelMapper) {
        this.contestService = contestService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/create")
    public String createContest(Model model) {
        if (!model.containsAttribute("contestCreateBindingModel")) {
            model.addAttribute("contestCreateBindingModel", new ContestCreateBindingModel());
        }
        return "add-contest";
    }

    @PostMapping("/create")
    public String createContestConfirm(@Valid @ModelAttribute(name = "contestCreateBindingModel")
                  ContestCreateBindingModel contestCreateBindingModel, BindingResult result, RedirectAttributes attributes,
                  @RequestParam(name = "file") MultipartFile file) {

        if (result.hasErrors()) {
            return "redirect:create";
        }

        return "/";
    }
}
