package com.krasimirkolchev.photomag.services.impl;

import com.krasimirkolchev.photomag.models.entities.Order;
import com.krasimirkolchev.photomag.models.serviceModels.OrderServiceModel;
import com.krasimirkolchev.photomag.repositories.OrderRepository;
import com.krasimirkolchev.photomag.services.OrderService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, ModelMapper modelMapper) {
        this.orderRepository = orderRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<OrderServiceModel> getAllOrders() {
        return this.orderRepository.findAll()
                .stream()
                .map(o -> this.modelMapper.map(o, OrderServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderServiceModel> getAllOrdersByUsername(String username) {
        return this.orderRepository.getOrdersByUser_UsernameOrderByPurchaseDateTime(username)
                .stream()
                .map(o -> this.modelMapper.map(o, OrderServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public OrderServiceModel getOrderById(String id) {
        return this.modelMapper.map(this.orderRepository.getOne(id), OrderServiceModel.class);
    }

    @Override
    public OrderServiceModel createOrder(OrderServiceModel orderServiceModel) {
        Order order = this.modelMapper.map(orderServiceModel, Order.class);

        return this.modelMapper.map(this.orderRepository.saveAndFlush(order), OrderServiceModel.class);
    }
}
