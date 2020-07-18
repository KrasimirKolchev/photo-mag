package com.krasimirkolchev.photomag.services;

import com.krasimirkolchev.photomag.models.entities.Product;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ProductService {

    Product addProduct(Product product, MultipartFile file) throws IOException;

    Product buyProduct(String id, String userId);

    List<Product> findByCategory(String category);

    boolean canBoughProduct(String id);
}
