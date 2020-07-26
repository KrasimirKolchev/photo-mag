package com.krasimirkolchev.photomag.services;

import com.krasimirkolchev.photomag.models.entities.CartItem;
import com.krasimirkolchev.photomag.models.serviceModels.CartItemServiceModel;
import com.krasimirkolchev.photomag.models.serviceModels.UserServiceModel;

public interface CartItemService {

    CartItem saveItem(CartItem cartItem);

    void deleteItem(String itemId);

    CartItemServiceModel getItemById(String itemId);
}
