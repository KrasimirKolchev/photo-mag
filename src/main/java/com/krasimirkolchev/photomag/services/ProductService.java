package com.krasimirkolchev.photomag.services;

import com.krasimirkolchev.photomag.models.entities.Product;
import com.krasimirkolchev.photomag.models.serviceModels.CartItemServiceModel;
import com.krasimirkolchev.photomag.models.serviceModels.ProductServiceModel;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ProductService {

    ProductServiceModel addProduct(ProductServiceModel product, List<MultipartFile> files) throws IOException;

//    List<Product> findByCategory(String category);

    List<Product> getProductsByCategoryName(String name);

    ProductServiceModel getProductById(String id);

    void decreaseProductQty(ProductServiceModel product, Integer quantity);

    void increaseProductQuantity(ProductServiceModel product, Integer quantity);

    ProductServiceModel editProduct(String id, ProductServiceModel productServiceModel);

    void deleteProduct(String id);

    List<ProductServiceModel> getAllProducts();

    List<ProductServiceModel> getProductsOrderedBy(String category, String order);
}
