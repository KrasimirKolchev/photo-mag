package com.krasimirkolchev.photomag.services.impl;

import com.krasimirkolchev.photomag.error.ProductCategoryNotFoundException;
import com.krasimirkolchev.photomag.models.bindingModels.CategoryAddBindingModel;
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
            ProductCategory dslrCamera = new ProductCategory("DSLR Cameras", "https://p1.akcdn.net/full/431753403.canon-eos-800d-ef-s-18-55mm-is-stm.jpg");
            ProductCategory lenses = new ProductCategory("Camera lenses", "https://magazin.photosynthesis.bg/152584-thickbox_default/obektiv-olympus-70-300mm-f-4-56.jpg");
            ProductCategory flashes = new ProductCategory("Camera flashes", "https://magazin.photosynthesis.bg/169553-thickbox_default/godox-tt350c-canon.jpg");
            ProductCategory bags = new ProductCategory("Camera bags", "https://magazin.photosynthesis.bg/139123-thickbox_default/chanta-lowepro-format-tlz-20-cheren.jpg");
            ProductCategory accessories = new ProductCategory("Accessories", "https://magazin.photosynthesis.bg/153795-thickbox_default/aksesoar-giottos-cl1001-komplekt-za-pochistvane-ot-5-chasti.jpg");
            ProductCategory tripods = new ProductCategory("Tripods", "https://magazin.photosynthesis.bg/148921-thickbox_default/stativ-velbon-m-47-tripod-kit.jpg");

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
