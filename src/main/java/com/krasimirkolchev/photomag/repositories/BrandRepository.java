package com.krasimirkolchev.photomag.repositories;

import com.krasimirkolchev.photomag.models.entities.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BrandRepository extends JpaRepository<Brand, String> {
    boolean existsByName(String name);
}
