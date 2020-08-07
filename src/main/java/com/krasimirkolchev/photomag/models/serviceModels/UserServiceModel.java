package com.krasimirkolchev.photomag.models.serviceModels;

import com.krasimirkolchev.photomag.models.entities.Role;

import java.util.LinkedHashSet;
import java.util.Set;

public class UserServiceModel extends BaseServiceModel {
    private String username;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private String profilePhoto;
    private Set<RoleServiceModel> authorities;
    private Set<AddressServiceModel> addresses;
    private ShoppingCartServiceModel shoppingCart;
    private boolean isDeleted;

    public UserServiceModel() {
    }

    public UserServiceModel(String username, String password, String email, String firstName, String lastName) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.addresses = new LinkedHashSet<>();
        this.isDeleted = false;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public Set<RoleServiceModel> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<RoleServiceModel> authorities) {
        this.authorities = authorities;
    }

    public Set<AddressServiceModel> getAddresses() {
        return addresses;
    }

    public void setAddresses(Set<AddressServiceModel> addresses) {
        this.addresses = addresses;
    }

    public ShoppingCartServiceModel getShoppingCart() {
        return shoppingCart;
    }

    public void setShoppingCart(ShoppingCartServiceModel shoppingCart) {
        this.shoppingCart = shoppingCart;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}
