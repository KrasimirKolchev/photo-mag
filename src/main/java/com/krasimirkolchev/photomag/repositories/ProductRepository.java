package com.krasimirkolchev.photomag.repositories;

import com.krasimirkolchev.photomag.models.entities.Product;
import com.krasimirkolchev.photomag.models.entities.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {

    List<Product> getAllByProductCategory_NameAndQuantityIsGreaterThanAndDeletedFalse(String category, Integer qty);

    List<Product> getAllByQuantityIsGreaterThanAndDeletedFalseOrderByBrand(Integer qty);

    List<Product> getAllByProductCategory_NameAndQuantityIsGreaterThanAndDeletedFalseOrderByPriceDesc(String category, int qty);

    List<Product> getAllByProductCategory_NameAndQuantityIsGreaterThanAndDeletedFalseOrderByPriceAsc(String category, int qty);

    List<Product> getAllByProductCategory_NameAndQuantityIsGreaterThanAndDeletedFalseOrderByNameAsc(String category, int qty);

    List<Product> getAllByProductCategory_NameAndQuantityIsGreaterThanAndDeletedFalseOrderByNameDesc(String category, int qty);

    boolean existsByName(String name);
}
