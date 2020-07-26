package com.krasimirkolchev.photomag.services;

import com.krasimirkolchev.photomag.models.entities.Product;
import com.krasimirkolchev.photomag.models.serviceModels.CartItemServiceModel;
import com.krasimirkolchev.photomag.models.serviceModels.ProductServiceModel;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ProductService {

    ProductServiceModel addProduct(ProductServiceModel product, List<MultipartFile> files) throws IOException;

    List<Product> findByCategory(String category);

    List<Product> getProductsByCategoryName(String name);

    ProductServiceModel getProductById(String id);

    void decreaseProductQty(Product item, Integer quantity);

    void increaseProductQuantity(Product product, Integer quantity);
}
