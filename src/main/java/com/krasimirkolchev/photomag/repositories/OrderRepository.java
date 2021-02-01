package com.krasimirkolchev.photomag.repositories;

import com.krasimirkolchev.photomag.models.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {
    List<Order> getOrdersByUser_UsernameOrderByPurchaseDateTime(String username);

    List<Order> getOrdersByPurchaseDateTimeGreaterThanAndPurchaseDateTimeLessThanOrderByPurchaseDateTimeAsc(LocalDateTime st, LocalDateTime end);

}
