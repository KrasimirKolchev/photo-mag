package com.krasimirkolchev.photomag.models.bindingModels;

public class UserRoleAddBindingModel {
    private String username;
    private String role;

    public UserRoleAddBindingModel() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
