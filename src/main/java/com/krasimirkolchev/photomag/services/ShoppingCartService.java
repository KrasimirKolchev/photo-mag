package com.krasimirkolchev.photomag.services;

import com.krasimirkolchev.photomag.models.bindingModels.CartItemAddBindModel;
import com.krasimirkolchev.photomag.models.entities.ShoppingCart;
import com.krasimirkolchev.photomag.models.serviceModels.ShoppingCartServiceModel;
import com.krasimirkolchev.photomag.models.serviceModels.VoucherServiceModel;

public interface ShoppingCartService {

    void addItemToCart(CartItemAddBindModel cartItemAddBindModel, String username);

    void removeItemFromCart(String productId, String username);

    void retrieveShoppingCart(ShoppingCartServiceModel shoppingCart);
}
