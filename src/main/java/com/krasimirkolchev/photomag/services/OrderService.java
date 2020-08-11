package com.krasimirkolchev.photomag.services;

import com.krasimirkolchev.photomag.models.bindingModels.AddressGetBindingModel;
import com.krasimirkolchev.photomag.models.serviceModels.CartItemServiceModel;
import com.krasimirkolchev.photomag.models.serviceModels.OrderItemServiceModel;
import com.krasimirkolchev.photomag.models.serviceModels.OrderServiceModel;
import com.stripe.model.Charge;

import java.security.Principal;
import java.util.List;

public interface OrderService {

    List<OrderServiceModel> getAllOrders();

    List<OrderServiceModel> getAllOrdersByUsername(String username);

    OrderServiceModel createOrder(OrderServiceModel orderServiceModel);

    OrderServiceModel generateOrder(Charge charge, Principal principal, String addressId);

}
