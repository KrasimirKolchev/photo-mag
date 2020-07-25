package com.krasimirkolchev.photomag.services.impl;

import com.krasimirkolchev.photomag.models.entities.CartItem;
import com.krasimirkolchev.photomag.models.serviceModels.UserServiceModel;
import com.krasimirkolchev.photomag.repositories.CartItemRepository;
import com.krasimirkolchev.photomag.services.CartItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartItemServiceImpl implements CartItemService {
    private final CartItemRepository cartItemRepository;

    @Autowired
    public CartItemServiceImpl(CartItemRepository cartItemRepository) {
        this.cartItemRepository = cartItemRepository;
    }

    @Override
    public CartItem saveItem(CartItem cartItem) {
        cartItem.setSubTotal(cartItem.getItem().getPrice() * cartItem.getQuantity());

        return this.cartItemRepository.save(cartItem);
    }

}
