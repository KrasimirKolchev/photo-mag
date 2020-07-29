package com.krasimirkolchev.photomag.models.bindingModels;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

public class AddressAddBindingModel {
    private String street;
    private String city;
    private String country;

    public AddressAddBindingModel() {
    }

    @NotBlank(message = "")
    @Length(min = 3, max = 50, message = "")
    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    @NotBlank(message = "")
    @Length(min = 3, max = 20, message = "")
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @NotBlank(message = "")
    @Length(min = 3, max = 20, message = "")
    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
