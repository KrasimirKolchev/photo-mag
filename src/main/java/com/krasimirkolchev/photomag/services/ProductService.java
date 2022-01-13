package com.krasimirkolchev.photomag.services;

import com.krasimirkolchev.photomag.models.serviceModels.ProductServiceModel;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ProductService {

    ProductServiceModel addProduct(ProductServiceModel product, List<MultipartFile> files) throws IOException;

//    List<Product> findByCategory(String category);

    List<ProductServiceModel> getProductsByCategoryName(String name);

    ProductServiceModel getProductById(String id);

    void decreaseProductQty(ProductServiceModel product, Integer quantity);

    void increaseProductQuantity(ProductServiceModel product, Integer quantity);

    ProductServiceModel editProduct(String id, ProductServiceModel productServiceModel);

    void deleteProduct(String id);

    List<ProductServiceModel> getAllActiveProducts();

    List<ProductServiceModel> getAllProducts();

//    List<ProductServiceModel> getProductsOrderedBy(String category, String order);
}
