package com.krasimirkolchev.photomag.models.bindingModels;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

public class AddressAddBindingModel {
    private String street;
    private String city;
    private String country;

    public AddressAddBindingModel() {
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
