package com.krasimirkolchev.photomag.services;

import com.krasimirkolchev.photomag.models.serviceModels.BrandServiceModel;

import java.util.List;

public interface BrandService {

    BrandServiceModel createBrand(BrandServiceModel brandServiceModel);

    List<BrandServiceModel> getAllBrands();
}
