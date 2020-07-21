package com.krasimirkolchev.photomag.services;

import com.krasimirkolchev.photomag.models.entities.Order;

import java.util.List;

public interface OrderService {

    Order createOrder();

    List<Order> getAllOrders(String username);

    Order getOrderById(String id);

    List<Order> getAllOrders();
}
