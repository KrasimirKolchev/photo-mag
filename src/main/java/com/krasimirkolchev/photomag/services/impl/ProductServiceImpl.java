package com.krasimirkolchev.photomag.services.impl;

import com.krasimirkolchev.photomag.models.entities.Product;
import com.krasimirkolchev.photomag.models.entities.User;
import com.krasimirkolchev.photomag.models.serviceModels.CartItemServiceModel;
import com.krasimirkolchev.photomag.models.serviceModels.ProductServiceModel;
import com.krasimirkolchev.photomag.repositories.ProductRepository;
import com.krasimirkolchev.photomag.services.ProductCategoryService;
import com.krasimirkolchev.photomag.services.ProductService;
import com.krasimirkolchev.photomag.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CloudinaryServiceImpl cloudinaryService;
    private final ProductCategoryService productCategoryService;
    private final UserService userService;
    private final ModelMapper modelMapper;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, UserService userService, ModelMapper modelMapper
            , CloudinaryServiceImpl cloudinaryService, ProductCategoryService productCategoryService) {
        this.productRepository = productRepository;
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.cloudinaryService = cloudinaryService;
        this.productCategoryService = productCategoryService;
    }

    @Override
    public ProductServiceModel addProduct(ProductServiceModel productServiceModel, List<MultipartFile> files) throws IOException {

        Product product = this.modelMapper.map(productServiceModel, Product.class);
        product.setProductGallery((this.cloudinaryService.createPhotos(files, "products", product.getName())));
        product.setMainPhoto(product.getProductGallery().get(0));

        return this.modelMapper.map(this.productRepository.save(product), ProductServiceModel.class);
    }

//    @Override
//    public List<Product> findByCategory(String category) {
//        return this.productRepository.getAllByProductCategory_NameAndQuantityIsGreaterThan(category, 0);
//    }

    @Override
    public List<Product> getProductsByCategoryName(String name) {
        return this.productRepository.getAllByProductCategory_NameAndQuantityIsGreaterThanAndDeletedFalse(name, 0);
    }

    @Override
    public ProductServiceModel getProductById(String id) {
        return this.modelMapper
                .map(this.productRepository.getOne(id), ProductServiceModel.class);
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
        Product product = this.productRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(""));
        product.setQuantity(productServiceModel.getQuantity());
        product.setPrice(productServiceModel.getPrice());
        product.setDescription(productServiceModel.getDescription());
        return this.modelMapper.map(this.productRepository.save(product), ProductServiceModel.class);
    }

    @Override
    public void deleteProduct(String id) {
        Product product = this.productRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(""));
        product.setDeleted(true);
        this.productRepository.save(product);
    }
}
