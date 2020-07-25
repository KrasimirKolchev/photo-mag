package com.krasimirkolchev.photomag.services;

import com.krasimirkolchev.photomag.models.entities.Address;
import com.krasimirkolchev.photomag.models.serviceModels.AddressServiceModel;

import java.security.Principal;
import java.util.List;

public interface AddressService {

//    AddressServiceModel createAddress(AddressServiceModel addressServiceModel);

    AddressServiceModel getAddressByUserId(String id);

    List<AddressServiceModel> getAllAddressesByUserId();
}
