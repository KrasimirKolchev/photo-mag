package com.krasimirkolchev.photomag.services.impl;

import com.krasimirkolchev.photomag.error.ProductNotFoundException;
import com.krasimirkolchev.photomag.models.entities.Product;
import com.krasimirkolchev.photomag.models.serviceModels.ProductServiceModel;
import com.krasimirkolchev.photomag.repositories.ProductRepository;
import com.krasimirkolchev.photomag.services.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CloudinaryServiceImpl cloudinaryService;
    private final ModelMapper modelMapper;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, ModelMapper modelMapper
            , CloudinaryServiceImpl cloudinaryService) {
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
        this.cloudinaryService = cloudinaryService;
    }

    @Override
    public ProductServiceModel addProduct(ProductServiceModel productServiceModel, List<MultipartFile> files) throws IOException {

        Product product = this.modelMapper.map(productServiceModel, Product.class);
        product.setProductGallery(this.cloudinaryService.createPhotos(files, "products", product.getName()));
        product.setMainPhoto(product.getProductGallery().get(0));

        return this.modelMapper.map(this.productRepository.save(product), ProductServiceModel.class);
    }

    @Override
    public List<ProductServiceModel> getProductsByCategoryName(String name) {
        return this.productRepository.getAllByProductCategory_NameAndQuantityIsGreaterThanAndDeletedFalse(name, 0)
                .stream()
                .map(p -> this.modelMapper.map(p, ProductServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public ProductServiceModel getProductById(String id) {
        return this.modelMapper
                .map(this.productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException("product not found"))
                        , ProductServiceModel.class);
    }

    @Override
    public void decreaseProductQty(ProductServiceModel product, Integer quantity) {
        product.setQuantity(product.getQuantity() - quantity);
        this.productRepository.save(this.modelMapper.map(product, Product.class));
    }

    @Override
    public void increaseProductQuantity(ProductServiceModel productServiceModel, Integer quantity) {
        ProductServiceModel product = this.getProductById(productServiceModel.getId());
        product.setQuantity(product.getQuantity() + quantity);
        this.productRepository.save(this.modelMapper.map(product, Product.class));
    }

    @Override
    public ProductServiceModel editProduct(String id, ProductServiceModel productServiceModel) {
        Product product = this.productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("product not found"));
        product.setQuantity(productServiceModel.getQuantity());
        product.setPrice(productServiceModel.getPrice());
        product.setDescription(productServiceModel.getDescription());
        return this.modelMapper.map(this.productRepository.save(product), ProductServiceModel.class);
    }

    @Override
    public void deleteProduct(String id) {
        Product product = this.productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("product not found"));
        product.setDeleted(true);
        this.productRepository.save(product);
    }

    @Override
    public List<ProductServiceModel> getAllActiveProducts() {
        return this.productRepository.getAllByQuantityIsGreaterThanAndDeletedFalseOrderByBrandName(0)
                .stream()
                .map(p -> this.modelMapper.map(p, ProductServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductServiceModel> getAllProducts() {
        return this.productRepository.findAll()
                .stream()
                .map(p -> this.modelMapper.map(p, ProductServiceModel.class))
                .collect(Collectors.toList());
    }

//    @Override
//    public List<ProductServiceModel> getProductsOrderedBy(String category, String order) {
//        switch (order) {
//            case "priced":
//                return this.productRepository
//                        .getAllByProductCategory_NameAndQuantityIsGreaterThanAndDeletedFalseOrderByPriceDesc(category, 0)
//                        .stream()
//                        .map(p -> this.modelMapper.map(p, ProductServiceModel.class))
//                        .collect(Collectors.toList());
//
//            case "pricea":
//                return this.productRepository
//                        .getAllByProductCategory_NameAndQuantityIsGreaterThanAndDeletedFalseOrderByPriceAsc(category, 0)
//                        .stream()
//                        .map(p -> this.modelMapper.map(p, ProductServiceModel.class))
//                        .collect(Collectors.toList());
//        }
//        return null;
//    }
}
