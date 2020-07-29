package com.krasimirkolchev.photomag.models.bindingModels;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

public class UserLoginBindingModel {
    private String username;
    private String password;

    public UserLoginBindingModel() {
    }

    @NotBlank(message = "")
    @Length(min = 6, max = 16, message = "Username must be between 6 and 16 characters")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @NotBlank(message = "")
    @Length(min = 6, max = 12, message = "Password must be between 6 and 12 characters")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
