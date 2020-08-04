package com.krasimirkolchev.photomag.services.impl;

import com.krasimirkolchev.photomag.models.bindingModels.AddressGetBindingModel;
import com.krasimirkolchev.photomag.models.entities.Order;
import com.krasimirkolchev.photomag.models.serviceModels.OrderItemServiceModel;
import com.krasimirkolchev.photomag.models.serviceModels.OrderServiceModel;
import com.krasimirkolchev.photomag.models.serviceModels.UserServiceModel;
import com.krasimirkolchev.photomag.repositories.OrderRepository;
import com.krasimirkolchev.photomag.services.*;
import com.stripe.model.Charge;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final UserService userService;
    private final ShoppingCartService shoppingCartService;
    private final OrderItemService orderItemService;
    private final ProductService productService;
    private final ModelMapper modelMapper;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, UserService userService, ShoppingCartService shoppingCartService,
                            OrderItemService orderItemService, ProductService productService, ModelMapper modelMapper) {
        this.orderRepository = orderRepository;
        this.userService = userService;
        this.shoppingCartService = shoppingCartService;
        this.orderItemService = orderItemService;
        this.productService = productService;
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
    public OrderServiceModel createOrder(OrderServiceModel orderServiceModel) {
        Order order = this.modelMapper.map(orderServiceModel, Order.class);
        return this.modelMapper.map(this.orderRepository.save(order), OrderServiceModel.class);
    }

    @Override
    public OrderServiceModel generateOrder(Charge charge, Principal principal, AddressGetBindingModel addressGetBindingModel) {
        UserServiceModel userServiceModel = this.modelMapper
                .map(this.userService.getUserByUsername(principal.getName()), UserServiceModel.class);

        OrderServiceModel orderServiceModel = new OrderServiceModel();
        orderServiceModel.setOrderItems(userServiceModel.getShoppingCart().getItems()
                .stream()
                .map(ci ->  {
                    OrderItemServiceModel orderItemServiceModel = this.modelMapper.map(ci, OrderItemServiceModel.class);
                    orderItemServiceModel.setOrderItem(this.productService.getProductById(ci.getItem().getId()));
                    return this.orderItemService.addOrderItem(orderItemServiceModel);
                })
                .collect(Collectors.toList()));

        orderServiceModel.setUser(userServiceModel);
        orderServiceModel.setChargeId(charge.getId());
        orderServiceModel.setTotalAmount(userServiceModel.getShoppingCart().getTotalCartAmount());
        orderServiceModel.setAddress(userServiceModel.getAddresses()
                .stream()
                .filter(a -> a.getId().equals(addressGetBindingModel.getAddressId()))
                .findFirst()
                .orElse(null));
        this.createOrder(orderServiceModel);
        this.shoppingCartService.retrieveShoppingCart(userServiceModel.getShoppingCart());

        return orderServiceModel;
    }

}
