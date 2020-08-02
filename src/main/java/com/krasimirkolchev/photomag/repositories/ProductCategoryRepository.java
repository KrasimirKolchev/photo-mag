package com.krasimirkolchev.photomag.repositories;

import com.krasimirkolchev.photomag.models.entities.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, String> {

    ProductCategory getByName(String name);

    boolean existsByName(String name);
}
