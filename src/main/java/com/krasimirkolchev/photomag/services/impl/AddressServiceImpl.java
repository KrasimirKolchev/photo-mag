package com.krasimirkolchev.photomag.services.impl;

import com.krasimirkolchev.photomag.models.entities.Address;
import com.krasimirkolchev.photomag.models.serviceModels.AddressServiceModel;
import com.krasimirkolchev.photomag.repositories.AddressRepository;
import com.krasimirkolchev.photomag.services.AddressService;
import com.krasimirkolchev.photomag.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
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

//    @Override
//    public AddressServiceModel createAddress(AddressServiceModel addressServiceModel) {
//        Address address = this.modelMapper.map(addressServiceModel, Address.class);
//        return this.modelMapper.map(this.addressRepository.save(address), AddressServiceModel.class);
//    }

    @Override
    public AddressServiceModel getAddressByUserId(String id) {
        return this.modelMapper.map(this.addressRepository.getOne(id), AddressServiceModel.class);
    }

    @Override
    public List<AddressServiceModel> getAllAddressesByUserId() {
        return this.addressRepository.findAll()
                .stream().map(a -> this.modelMapper.map(a, AddressServiceModel.class))
                .collect(Collectors.toList());
    }
}
