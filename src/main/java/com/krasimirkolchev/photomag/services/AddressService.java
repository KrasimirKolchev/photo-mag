package com.krasimirkolchev.photomag.services;

import com.krasimirkolchev.photomag.models.serviceModels.AddressServiceModel;

import java.util.List;

public interface AddressService {

    AddressServiceModel createAddress(AddressServiceModel addressServiceModel);

    AddressServiceModel getAddressById(String id);

}
