package com.krasimirkolchev.photomag.services;

import com.krasimirkolchev.photomag.models.entities.Address;

import java.util.List;

public interface AddressService {

    Address createAddress(Address address);

    Address getAddress(String id);

    List<Address> getAllAddresses();
}
