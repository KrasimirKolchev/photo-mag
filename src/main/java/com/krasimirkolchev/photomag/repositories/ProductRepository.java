package com.krasimirkolchev.photomag.repositories;

import com.krasimirkolchev.photomag.models.entities.Product;
import com.krasimirkolchev.photomag.models.entities.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {

    List<Product> getAllByProductCategory_NameAndQuantityIsGreaterThan(String category, Integer qty);
}
