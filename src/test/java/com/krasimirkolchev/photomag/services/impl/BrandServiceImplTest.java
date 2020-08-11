package com.krasimirkolchev.photomag.services.impl;

import com.krasimirkolchev.photomag.error.BrandNotFoundException;
import com.krasimirkolchev.photomag.models.entities.Brand;
import com.krasimirkolchev.photomag.models.serviceModels.BrandServiceModel;
import com.krasimirkolchev.photomag.repositories.BrandRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
class BrandServiceImplTest {
    private BrandServiceImpl brandService;
    private BrandRepository brandRepository;
    private ModelMapper modelMapper;
    private List<Brand> brands;

    @BeforeEach
    void setUp() {
        modelMapper = Mockito.mock(ModelMapper.class);

        Brand one = new Brand();
        one.setName("Nikon");
        one.setId("1");
        Brand two = new Brand();
        two.setName("Canon");
        two.setId("2");
        brands = List.of(one, two);
        brandRepository = Mockito.mock(BrandRepository.class);
        Mockito.when(brandRepository.findAll()).thenReturn(brands);
        Mockito.when(brandRepository.findById(anyString())).thenReturn(Optional.of(one));

        brandService = new BrandServiceImpl(brandRepository, modelMapper);

        ModelMapper mapper = new ModelMapper();

        Mockito.when(modelMapper.map(any(BrandServiceModel.class), eq(Brand.class)))
                .thenAnswer(invocation -> mapper.map(invocation.getArguments()[0], Brand.class));

        Mockito.when(modelMapper.map(any(Brand.class), eq(BrandServiceModel.class)))
                .thenAnswer(invocation -> mapper.map(invocation.getArguments()[0], BrandServiceModel.class));
    }

    @Test
    void createBrandWhenValidReturnsCreatedBrand() {
        BrandServiceModel brandServiceModel = new BrandServiceModel();
        brandServiceModel.setName("Canon");

        Mockito.when(brandRepository.save(any())).thenReturn(any());
        Mockito.when(brandService.createBrand(any())).thenReturn(brandServiceModel);

        BrandServiceModel brand = brandService.createBrand(brandServiceModel);

        assertEquals(brandServiceModel.getName(), brand.getName());
    }

    @Test
    void getAllBrands() {
        List<BrandServiceModel> brands1 = brandService.getAllBrands();

        assertEquals(brands.size(), brands1.size());
        assertEquals("Canon", brands1.get(0).getName());
        assertEquals("Nikon", brands1.get(1).getName());
    }

    @Test
    void getBrandByIdWhenValidReturnBrand() {
        BrandServiceModel exp = brandService.getBrandById("1");

        assertEquals("Nikon", exp.getName());
    }

    @Test
    void getBrandByIdWhenInvalidThrows() {
        when(this.brandRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(BrandNotFoundException.class, () -> this.brandService.getBrandById("invalid"));
    }
}