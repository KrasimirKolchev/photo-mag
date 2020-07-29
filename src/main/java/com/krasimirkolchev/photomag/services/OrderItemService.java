package com.krasimirkolchev.photomag.services;

import com.krasimirkolchev.photomag.models.serviceModels.OrderItemServiceModel;

public interface OrderItemService {
    OrderItemServiceModel addOrderItem(OrderItemServiceModel orderItemServiceModel);
}
