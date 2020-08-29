package com.krasimirkolchev.photomag.services.impl;

import com.krasimirkolchev.photomag.error.ProductCategoryNotFoundException;
import com.krasimirkolchev.photomag.models.entities.ProductCategory;
import com.krasimirkolchev.photomag.models.serviceModels.ProductCategoryServiceModel;
import com.krasimirkolchev.photomag.repositories.ProductCategoryRepository;
import com.krasimirkolchev.photomag.services.ProductCategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.krasimirkolchev.photomag.common.CommonMessages.*;

@Service
public class ProductCategoryServiceImpl implements ProductCategoryService {
    private final ProductCategoryRepository productCategoryRepository;
    private final CloudinaryServiceImpl cloudinaryService;
    private final ModelMapper modelMapper;

    @Autowired
    public ProductCategoryServiceImpl(ProductCategoryRepository productCategoryRepository, CloudinaryServiceImpl cloudinaryService, ModelMapper modelMapper) {
        this.productCategoryRepository = productCategoryRepository;
        this.cloudinaryService = cloudinaryService;
        this.modelMapper = modelMapper;
    }

    @PostConstruct
    private void init() {
        if (this.productCategoryRepository.count() == 0) {
            ProductCategory dslrCamera = new ProductCategory("DSLR Cameras", DSLR_CAMERA_URL);
            ProductCategory lenses = new ProductCategory("Camera lenses", CAMERA_LENSES_URL);
            ProductCategory flashes = new ProductCategory("Camera flashes", CAMERA_FLASHES_URL);
            ProductCategory bags = new ProductCategory("Camera bags", CAMERA_BAGS_URL);
            ProductCategory accessories = new ProductCategory("Accessories", CAMERA_ACCESSORIES);
            ProductCategory tripods = new ProductCategory("Tripods", CAMERA_TRIPODS);

            this.productCategoryRepository.saveAll(List.of(dslrCamera, lenses, flashes, bags, accessories, tripods));
        }
    }

    @Override
    public List<ProductCategoryServiceModel> getAllCategories() {
        return this.productCategoryRepository.findAll()
                .stream()
                .map(c -> this.modelMapper.map(c, ProductCategoryServiceModel.class))
                .sorted(Comparator.comparing(ProductCategoryServiceModel::getName))
                .collect(Collectors.toList());
    }

    @Override
    public ProductCategoryServiceModel getCategoryById(String id) {
        ProductCategory category = this.productCategoryRepository.findById(id)
                .orElseThrow(() -> new ProductCategoryNotFoundException("Category not found!"));
        return this.modelMapper.map(category, ProductCategoryServiceModel.class);
    }

    @Override
    public ProductCategoryServiceModel addCategory(ProductCategoryServiceModel categoryServiceModel, MultipartFile file) throws IOException {

        ProductCategory category = this.modelMapper.map(categoryServiceModel, ProductCategory.class);
        category.setPhoto(this.cloudinaryService
                .createPhoto(file, "categories", category.getName()));

        return this.modelMapper.map(this.productCategoryRepository.save(category), ProductCategoryServiceModel.class);
    }

}
