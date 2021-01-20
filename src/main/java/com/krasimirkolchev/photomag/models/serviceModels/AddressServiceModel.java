package com.krasimirkolchev.photomag.models.serviceModels;

public class AddressServiceModel extends BaseServiceModel {
    private String street;
    private String city;
    private String country;

    public AddressServiceModel() {
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

    @Override
    public String toString() {
        return this.getCountry() + ", " + this.getCity() + ", " + this.getStreet();
    }
}
