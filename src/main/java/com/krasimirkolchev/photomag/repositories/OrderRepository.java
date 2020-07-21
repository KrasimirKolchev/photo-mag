package com.krasimirkolchev.photomag.repositories;

import com.krasimirkolchev.photomag.models.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {

    List<Order> getAllByUser_Id(String userId);
}
