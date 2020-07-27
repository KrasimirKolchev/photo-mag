package com.krasimirkolchev.photomag.services;

import com.krasimirkolchev.photomag.models.serviceModels.CartItemServiceModel;

public interface CartItemService {

    CartItemServiceModel saveItem(CartItemServiceModel cartItemServiceModel);

    void deleteItem(String itemId);

    CartItemServiceModel getItemById(String itemId);
}
