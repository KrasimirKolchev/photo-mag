package com.krasimirkolchev.photomag.services.impl;

import com.krasimirkolchev.photomag.models.entities.OrderItems;
import com.krasimirkolchev.photomag.models.serviceModels.OrderItemServiceModel;
import com.krasimirkolchev.photomag.repositories.OrderItemRepository;
import com.krasimirkolchev.photomag.services.OrderItemService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderItemServiceImpl implements OrderItemService {
    private final OrderItemRepository orderItemRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public OrderItemServiceImpl(OrderItemRepository orderItemRepository, ModelMapper modelMapper) {
        this.orderItemRepository = orderItemRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public OrderItemServiceModel addOrderItem(OrderItemServiceModel orderItemServiceModel) {
        OrderItems orderItems = this.modelMapper.map(orderItemServiceModel, OrderItems.class);
        return this.modelMapper.map(this.orderItemRepository.save(orderItems), OrderItemServiceModel.class);
    }
}
