package com.krasimirkolchev.photomag.models.bindingModels;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

public class AddressAddBindingModel {
    private String street;
    private String city;
    private String country;

    public AddressAddBindingModel() {
    }

    @NotBlank(message = "Street cannot be empty!")
    @Length(min = 3, max = 50, message = "Street must be between 3 and 50 symbols long!")
    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    @NotBlank(message = "City cannot be empty!")
    @Length(min = 3, max = 20, message = "City name must be between 3 and 20 symbols long!")
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @NotBlank(message = "Country cannot be empty!")
    @Length(min = 3, max = 20, message = "Country name must be between 3 and 20 symbols long!")
    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
