package com.krasimirkolchev.photomag.models.bindingModels;

import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class UserRegBindingModel {
    private String username;
    private String password;
    private String confirmPassword;
    private String email;
    private String firstName;
    private String lastName;
    private MultipartFile file;

    public UserRegBindingModel() {
    }


    @NotBlank(message = "Username cannot be empty!")
    @Length(min = 6, max = 16, message = "Username must be between 6 and 16 characters")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @NotBlank(message = "Password cannot be empty!")
    @Length(min = 6, max = 12, message = "Password must be between 6 and 12 characters")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @NotBlank(message = "Conform password cannot be empty!")
    @Length(min = 6, max = 12, message = "Confirm password must be between 6 and 12 characters")
    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    @NotBlank(message = "Email cannot be empty!")
    @Email(message = "Enter valid email address!")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }
}
