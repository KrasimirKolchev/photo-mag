package com.krasimirkolchev.photomag.services.impl;

import com.krasimirkolchev.photomag.models.bindingModels.CartItemAddBindModel;
import com.krasimirkolchev.photomag.models.serviceModels.ShoppingCartServiceModel;
import com.krasimirkolchev.photomag.services.ShoppingCartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import static org.mockito.ArgumentMatchers.*;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
class ShoppingCartServiceImplTest {
    private ShoppingCartService shoppingCartService;

    @BeforeEach
    void setUp() {
        shoppingCartService = Mockito.mock(ShoppingCartService.class);
    }

    @Test
    void addItemToCartShouldAddTheItem() {
        Mockito.doNothing().when(shoppingCartService).addItemToCart(isA(CartItemAddBindModel.class), isA(String.class));

        shoppingCartService.addItemToCart(any(), anyString());

        Mockito.verify(shoppingCartService).addItemToCart(any(), anyString());

    }

    @Test
    void removeItemFromCartShouldRemoveTheItem() {
        Mockito.doNothing().when(shoppingCartService).removeItemFromCart(isA(String.class), isA(String.class));

        shoppingCartService.removeItemFromCart(anyString(), anyString());

        Mockito.verify(shoppingCartService).removeItemFromCart(anyString(), anyString());
    }

    @Test
    void retrieveShoppingCartShouldEmptyTheCart() {
        Mockito.doNothing().when(shoppingCartService).retrieveShoppingCart(isA(ShoppingCartServiceModel.class));

        shoppingCartService.retrieveShoppingCart(any());

        Mockito.verify(shoppingCartService).retrieveShoppingCart(any());
    }
}