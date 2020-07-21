package com.krasimirkolchev.photomag.services.impl;

import com.krasimirkolchev.photomag.models.entities.Product;
import com.krasimirkolchev.photomag.models.entities.User;
import com.krasimirkolchev.photomag.models.entities.enums.ProductCategory;
import com.krasimirkolchev.photomag.models.serviceModels.ProductServiceModel;
import com.krasimirkolchev.photomag.repositories.ProductRepository;
import com.krasimirkolchev.photomag.services.ProductService;
import com.krasimirkolchev.photomag.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CloudinaryServiceImpl cloudinaryService;
    private final UserService userService;
    private final ModelMapper modelMapper;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, UserService userService, ModelMapper modelMapper
            , CloudinaryServiceImpl cloudinaryService) {
        this.productRepository = productRepository;
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.cloudinaryService = cloudinaryService;
    }

    @Override
    public ProductServiceModel addProduct(ProductServiceModel productServiceModel, List<MultipartFile> files) throws IOException {

        Product product = this.modelMapper.map(productServiceModel, Product.class);
        product.setProductGallery((this.cloudinaryService.createPhotos(files, product.getName())));

        return this.modelMapper.map(this.productRepository.save(product), ProductServiceModel.class);
    }

    @Override
    public Product buyProduct(String ProductId, String username) {
        Product product = this.productRepository.getOne(ProductId);
        //check user if have money

        User user = this.userService.getUserByUsername(username);
//        user.setBoughtProducts(List.of(product));

        product.setQuantity(product.getQuantity() - 1);

        return this.productRepository.save(product);
    }

    @Override
    public List<Product> findByCategory(String category) {
        return this.productRepository.getAllByProductCategoryEquals(ProductCategory.valueOf(category));
    }

    @Override
    public boolean canBoughProduct(String id) {
        return this.productRepository.getOne(id).getQuantity() > 0;
    }
}
