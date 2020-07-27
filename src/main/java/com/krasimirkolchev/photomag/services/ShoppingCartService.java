package com.krasimirkolchev.photomag.services;

import com.krasimirkolchev.photomag.models.bindingModels.CartItemAddBindModel;
import com.krasimirkolchev.photomag.models.entities.ShoppingCart;

public interface ShoppingCartService {

    void addItemToCart(CartItemAddBindModel cartItemAddBindModel, String username);

    void removeItemFromCart(String productId, String username);

}
