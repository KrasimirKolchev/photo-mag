package com.krasimirkolchev.photomag.services.impl;

import com.cloudinary.Cloudinary;
import com.krasimirkolchev.photomag.models.entities.Product;
import com.krasimirkolchev.photomag.models.entities.User;
import com.krasimirkolchev.photomag.models.entities.enums.ProductCategory;
import com.krasimirkolchev.photomag.repositories.ProductRepository;
import com.krasimirkolchev.photomag.services.PhotoService;
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
    private final PhotoService photoService;
    private final UserService userService;
    private final ModelMapper modelMapper;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, UserService userService, ModelMapper modelMapper
            , PhotoService photoService) {
        this.productRepository = productRepository;
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.photoService = photoService;
    }

    @Override
    public Product addProduct(Product product, MultipartFile file) throws IOException {

        Product prod = product; //map service model

        if (file == null || file.isEmpty() || file.getOriginalFilename().length() == 0) {
            throw new FileNotFoundException("File is empty!");
        }

        prod.getProductGallery().add(this.photoService.createPhoto(file, prod.getName()));
        return this.productRepository.save(product);
    }

    @Override
    public Product buyProduct(String ProductId, String username) {
        Product product = this.productRepository.getOne(ProductId);
        //check user if have money

        User user = this.userService.getUserByUsername(username);
        user.setBoughtProducts(List.of(product));

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
