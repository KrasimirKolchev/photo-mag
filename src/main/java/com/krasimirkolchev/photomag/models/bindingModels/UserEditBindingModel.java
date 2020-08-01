package com.krasimirkolchev.photomag.models.bindingModels;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

public class UserEditBindingModel {
    private String id;
    private String oldPassword;
    private String password;
    private String confirmPassword;
    private String firstName;
    private String lastName;

    public UserEditBindingModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @NotBlank(message = "Password cannot be empty!")
    @Length(min = 6, max = 12, message = "Password must be between 6 and 12 characters")
    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

//    @NotBlank(message = "Password cannot be empty!")
//    @Length(min = 6, max = 12, message = "Password must be between 6 and 12 characters")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

//    @NotBlank(message = "Conform password cannot be empty!")
//    @Length(min = 6, max = 12, message = "Confirm password must be between 6 and 12 characters")
    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    @NotBlank(message = "First name cannot be empty!")
    @Length(min = 3, max = 16, message = "First name must be between 3 and 16 symbols long!")
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @NotBlank(message = "Last name cannot be empty!")
    @Length(min = 3, max = 16, message = "Last name must be between 3 and 16 symbols long")
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

}
