package com.krasimirkolchev.photomag.repositories;

import com.krasimirkolchev.photomag.models.entities.OrderItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItems, String> {
}
