package com.krasimirkolchev.photomag.models.entities;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "users")
public class User extends BaseEntity {
    private String username;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private String profilePhoto;
    private Set<Role> authorities;
    private Set<Address> addresses;
    private ShoppingCart shoppingCart;
    private boolean isDeleted;
    private LocalDateTime deleteDate;

    public User() {
        this.addresses = new LinkedHashSet<>();
        this.isDeleted = false;
    }

    @Column(name = "username", unique = true, nullable = false)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Column(name = "password", nullable = false)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Column(name = "email", unique = true, nullable = false)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(name = "first_name", nullable = false)
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Column(name = "last_name", nullable = false)
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Column(name = "profile_photo", nullable = false)
    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    @ManyToMany(targetEntity = Role.class, fetch = FetchType.EAGER)
    public Set<Role> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<Role> authorities) {
        this.authorities = authorities;
    }

    @OneToMany(cascade = CascadeType.ALL)
    public Set<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(Set<Address> addresses) {
        this.addresses = addresses;
    }

    @OneToOne(targetEntity = ShoppingCart.class, cascade = CascadeType.ALL)
    public ShoppingCart getShoppingCart() {
        return shoppingCart;
    }

    public void setShoppingCart(ShoppingCart shoppingCart) {
        this.shoppingCart = shoppingCart;
    }

    @Column(name = "is_deleted", nullable = false)
    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    @Column(name = "delete_date")
    public LocalDateTime getDeleteDate() {
        return deleteDate;
    }

    public void setDeleteDate(LocalDateTime deleteDate) {
        this.deleteDate = deleteDate;
    }
}
