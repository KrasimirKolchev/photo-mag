package com.krasimirkolchev.photomag.services.impl;

import com.krasimirkolchev.photomag.models.bindingModels.CategoryAddBindingModel;
import com.krasimirkolchev.photomag.models.entities.Brand;
import com.krasimirkolchev.photomag.models.entities.ProductCategory;
import com.krasimirkolchev.photomag.models.serviceModels.BrandServiceModel;
import com.krasimirkolchev.photomag.models.serviceModels.ProductCategoryServiceModel;
import com.krasimirkolchev.photomag.repositories.ProductCategoryRepository;
import com.krasimirkolchev.photomag.services.ProductCategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
class ProductCategoryServiceImplTest {
    private ProductCategoryServiceImpl categoryService;
    private ProductCategoryRepository categoryRepository;
    private ModelMapper modelMapper;
    private ProductCategory productCategory;
    private CloudinaryServiceImpl cloudinaryService;
    private List<ProductCategory> categories;
    private MultipartFile file;

    @BeforeEach
    void setUp() throws IOException {
        modelMapper = Mockito.mock(ModelMapper.class);
        productCategory = new ProductCategory();
        productCategory.setName("Items");
        productCategory.setPhoto("photo");

        ModelMapper mapper = new ModelMapper();

        ByteArrayInputStream content = new ByteArrayInputStream("content".getBytes());
        file = new MockMultipartFile("file", "file.jpg", "image/jpeg", content);

        categories = List.of(productCategory);
        categoryRepository = Mockito.mock(ProductCategoryRepository.class);
        cloudinaryService = Mockito.mock(CloudinaryServiceImpl.class);

        categoryService = new ProductCategoryServiceImpl(categoryRepository, cloudinaryService, modelMapper);

        Mockito.when(categoryRepository.existsByName(anyString())).thenReturn(false);
        Mockito.when(categoryRepository.findById(anyString())).thenReturn(Optional.of(productCategory));
        Mockito.when(categoryRepository.findAll()).thenReturn(categories);
        Mockito.when(cloudinaryService.createPhoto(any(), anyString(), anyString())).thenReturn("asdasd");
        Mockito.when(categoryRepository.save(any())).thenReturn(productCategory);

        Mockito.when(modelMapper.map(any(ProductCategoryServiceModel.class), eq(ProductCategory.class)))
                .thenAnswer(invocation -> mapper.map(invocation.getArguments()[0], ProductCategory.class));

        Mockito.when(modelMapper.map(any(ProductCategory.class), eq(ProductCategoryServiceModel.class)))
                .thenAnswer(invocation -> mapper.map(invocation.getArguments()[0], ProductCategoryServiceModel.class));
    }

    @Test
    void getAllCategories() {
        List<ProductCategoryServiceModel> exp = categoryService.getAllCategories();
        assertEquals(exp.size(), categories.size());
    }

    @Test
    void getCategoryById() {
        ProductCategoryServiceModel exp = categoryService.getCategoryById(anyString());
        assertEquals(exp.getName(), "Items");
    }

    @Test
    void addCategory() throws IOException {
        ProductCategoryServiceModel act = new ProductCategoryServiceModel();
        act.setName("Items");

        ProductCategoryServiceModel exp = categoryService.addCategory(act, file);

        assertEquals(exp.getName(), act.getName());
    }

}