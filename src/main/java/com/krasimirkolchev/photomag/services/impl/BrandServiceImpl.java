package com.krasimirkolchev.photomag.services.impl;

import com.krasimirkolchev.photomag.models.entities.Brand;
import com.krasimirkolchev.photomag.models.serviceModels.BrandServiceModel;
import com.krasimirkolchev.photomag.repositories.BrandRepository;
import com.krasimirkolchev.photomag.services.BrandService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BrandServiceImpl implements BrandService {
    private final BrandRepository brandRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public BrandServiceImpl(BrandRepository brandRepository, ModelMapper modelMapper) {
        this.brandRepository = brandRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public BrandServiceModel createBrand(BrandServiceModel brandServiceModel) {
        Brand brand = this.modelMapper.map(brandServiceModel, Brand.class);
        return this.modelMapper.map(this.brandRepository.save(brand), BrandServiceModel.class);
    }

    @Override
    public List<BrandServiceModel> getAllBrands() {
        return this.brandRepository.findAll()
                .stream()
                .map(b -> this.modelMapper.map(b, BrandServiceModel.class))
                .collect(Collectors.toList());
    }
}
