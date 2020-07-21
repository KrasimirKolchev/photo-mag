package com.krasimirkolchev.photomag.services.impl;

import com.krasimirkolchev.photomag.models.entities.Order;
import com.krasimirkolchev.photomag.repositories.OrderItemRepository;
import com.krasimirkolchev.photomag.repositories.OrderRepository;
import com.krasimirkolchev.photomag.services.OrderService;
import com.krasimirkolchev.photomag.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final UserService userService;
    private final ModelMapper modelMapper;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, OrderItemRepository orderItemRepository, UserService userService
            , ModelMapper modelMapper) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @Override
    public Order createOrder() {
        Order order = new Order();

        return this.orderRepository.save(order);
    }

    @Override
    public List<Order> getAllOrders(String username) {
        return this.orderRepository.getAllByUser_Id(this.userService.getUserByUsername(username).getId());
    }

    @Override
    public Order getOrderById(String id) {
        return this.orderRepository.findById(id).orElse(null);
    }

    @Override
    public List<Order> getAllOrders() {
        return this.orderRepository.findAll();
    }
}
