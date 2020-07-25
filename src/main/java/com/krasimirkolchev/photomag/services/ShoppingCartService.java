package com.krasimirkolchev.photomag.services;

import com.krasimirkolchev.photomag.models.bindingModels.CartItemAddBindModel;

public interface ShoppingCartService {

    void addItemToCart(CartItemAddBindModel cartItemAddBindModel, String username);
}
