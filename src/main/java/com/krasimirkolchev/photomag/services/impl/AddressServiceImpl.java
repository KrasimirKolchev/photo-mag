package com.krasimirkolchev.photomag.services.impl;

import com.krasimirkolchev.photomag.models.entities.Address;
import com.krasimirkolchev.photomag.repositories.AddressRepository;
import com.krasimirkolchev.photomag.services.AddressService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public Address createAddress(Address address) {
        //map service to binding

        return this.addressRepository.save(address);
    }

    @Override
    public Address getAddress(String id) {
        return this.addressRepository.getOne(id);
    }

    @Override
    public List<Address> getAllAddresses() {
        return this.addressRepository.findAll();
    }
}
