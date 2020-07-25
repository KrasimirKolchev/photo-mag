package com.krasimirkolchev.photomag.repositories;

import com.krasimirkolchev.photomag.models.entities.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, String> {

}
