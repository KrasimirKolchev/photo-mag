package com.krasimirkolchev.photomag.models.serviceModels;

import com.krasimirkolchev.photomag.models.entities.Contest;
import com.krasimirkolchev.photomag.models.entities.Role;
import com.krasimirkolchev.photomag.models.entities.ShoppingCart;

import java.util.List;
import java.util.Set;

public class UserServiceModel extends BaseServiceModel {
    private String username;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private String profilePhoto;
    private Set<Role> authorities;
    private Set<AddressServiceModel> addresses;
    private List<String> gallery;
    private List<Contest> contests;
    private ShoppingCart shoppingCart;

    public UserServiceModel() {
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

    public Set<Role> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<Role> authorities) {
        this.authorities = authorities;
    }

    public Set<AddressServiceModel> getAddresses() {
        return addresses;
    }

    public void setAddresses(Set<AddressServiceModel> addresses) {
        this.addresses = addresses;
    }

    public List<String> getGallery() {
        return gallery;
    }

    public void setGallery(List<String> gallery) {
        this.gallery = gallery;
    }

    public List<Contest> getContests() {
        return contests;
    }

    public void setContests(List<Contest> contests) {
        this.contests = contests;
    }

    public ShoppingCart getShoppingCart() {
        return shoppingCart;
    }

    public void setShoppingCart(ShoppingCart shoppingCart) {
        this.shoppingCart = shoppingCart;
    }
}
