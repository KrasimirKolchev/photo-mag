package com.krasimirkolchev.photomag.services;

import com.krasimirkolchev.photomag.models.serviceModels.OrderServiceModel;

import java.util.List;

public interface OrderService {

    List<OrderServiceModel> getAllOrders();

    List<OrderServiceModel> getAllOrdersByUsername(String username);

    OrderServiceModel getOrderById(String id);

    OrderServiceModel createOrder(OrderServiceModel orderServiceModel);
}
