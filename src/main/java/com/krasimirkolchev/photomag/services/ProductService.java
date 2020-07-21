package com.krasimirkolchev.photomag.services;

import com.krasimirkolchev.photomag.models.entities.Product;
import com.krasimirkolchev.photomag.models.serviceModels.ProductServiceModel;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ProductService {

    ProductServiceModel addProduct(ProductServiceModel product, List<MultipartFile> files) throws IOException;

    Product buyProduct(String id, String userId);

    List<Product> findByCategory(String category);

    boolean canBoughProduct(String id);
}
