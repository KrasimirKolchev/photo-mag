package com.krasimirkolchev.photomag.models.bindingModels;

import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class CategoryAddBindingModel {
    private String name;
    private MultipartFile photo;

    public CategoryAddBindingModel() {
    }

    @NotBlank(message = "Category name cannot be empty!")
    @Length(min = 4, max = 20, message = "Category name must be between 4 and 20 symbols long!")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NotNull(message = "Category photo must be selected!")
    public MultipartFile getPhoto() {
        return photo;
    }

    public void setPhoto(MultipartFile photo) {
        this.photo = photo;
    }
}
