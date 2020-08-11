package com.krasimirkolchev.photomag.services.impl;

import com.krasimirkolchev.photomag.error.AddressNotFoundException;
import com.krasimirkolchev.photomag.models.entities.Address;
import com.krasimirkolchev.photomag.models.serviceModels.AddressServiceModel;
import com.krasimirkolchev.photomag.repositories.AddressRepository;
import com.krasimirkolchev.photomag.services.AddressService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AddressServiceImpl implements AddressService {
    private final AddressRepository addressRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public AddressServiceImpl(AddressRepository addressRepository, ModelMapper modelMapper) {
        this.addressRepository = addressRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public AddressServiceModel createAddress(AddressServiceModel addressServiceModel) {
        Address address = this.modelMapper.map(addressServiceModel, Address.class);
        return this.modelMapper.map(this.addressRepository.save(address), AddressServiceModel.class);
    }

    @Override
    public AddressServiceModel getAddressById(String id) {
        return this.modelMapper
                .map(this.addressRepository.findById(id).orElseThrow(() -> new AddressNotFoundException("Incorrect id!"))
                        , AddressServiceModel.class);
    }

}
