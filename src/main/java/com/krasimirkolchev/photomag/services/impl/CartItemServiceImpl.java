package com.krasimirkolchev.photomag.services.impl;

import com.krasimirkolchev.photomag.models.entities.CartItem;
import com.krasimirkolchev.photomag.models.serviceModels.CartItemServiceModel;
import com.krasimirkolchev.photomag.repositories.CartItemRepository;
import com.krasimirkolchev.photomag.services.CartItemService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartItemServiceImpl implements CartItemService {
    private final CartItemRepository cartItemRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public CartItemServiceImpl(CartItemRepository cartItemRepository, ModelMapper modelMapper) {
        this.cartItemRepository = cartItemRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public CartItemServiceModel saveItem(CartItemServiceModel cartItemServiceModel) {
        CartItem cartItem = this.modelMapper.map(cartItemServiceModel, CartItem.class);
        cartItem.setSubTotal(cartItemServiceModel.getItem().getPrice() * cartItemServiceModel.getQuantity());

        return this.modelMapper.map(this.cartItemRepository.save(cartItem), CartItemServiceModel.class);
    }

    @Override
    public void deleteItem(String itemId) {
        this.cartItemRepository.deleteById(itemId);
    }

    @Override
    public CartItemServiceModel getItemById(String itemId) {
        return this.modelMapper.map(this.cartItemRepository.getOne(itemId), CartItemServiceModel.class);
    }

}
