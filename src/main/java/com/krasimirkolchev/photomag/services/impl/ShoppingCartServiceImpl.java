package com.krasimirkolchev.photomag.services.impl;

import com.krasimirkolchev.photomag.models.bindingModels.CartItemAddBindModel;
import com.krasimirkolchev.photomag.models.entities.CartItem;
import com.krasimirkolchev.photomag.models.entities.Product;
import com.krasimirkolchev.photomag.models.entities.ShoppingCart;
import com.krasimirkolchev.photomag.models.entities.User;
import com.krasimirkolchev.photomag.models.serviceModels.CartItemServiceModel;
import com.krasimirkolchev.photomag.models.serviceModels.ProductServiceModel;
import com.krasimirkolchev.photomag.models.serviceModels.ShoppingCartServiceModel;
import com.krasimirkolchev.photomag.models.serviceModels.UserServiceModel;
import com.krasimirkolchev.photomag.repositories.ShoppingCartRepository;
import com.krasimirkolchev.photomag.services.CartItemService;
import com.krasimirkolchev.photomag.services.ProductService;
import com.krasimirkolchev.photomag.services.ShoppingCartService;
import com.krasimirkolchev.photomag.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final CartItemService cartItemService;
    private final ProductService productService;
    private final UserService userService;
    private final ModelMapper modelMapper;

    @Autowired
    public ShoppingCartServiceImpl(ShoppingCartRepository shoppingCartRepository,
                                   CartItemService cartItemService, ProductService productService, UserService userService, ModelMapper modelMapper) {
        this.shoppingCartRepository = shoppingCartRepository;
        this.cartItemService = cartItemService;
        this.productService = productService;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @Override
    public void addItemToCart(CartItemAddBindModel cartItemAddBindModel, String username) {
        UserServiceModel user = this.modelMapper
                .map(this.userService.getUserByUsername(username), UserServiceModel.class);

        ShoppingCartServiceModel shoppingCart = user.getShoppingCart();

        CartItemServiceModel item = shoppingCart.getItems().stream()
                .filter(ci -> ci.getItem().getId().equals(cartItemAddBindModel.getId()))
                .findFirst()
                .orElse(null);

        ProductServiceModel productServiceModel = this.productService.getProductById(cartItemAddBindModel.getId());

        if (item == null) {
            item = this.modelMapper.map(cartItemAddBindModel, CartItemServiceModel.class);
            item.setItem(productServiceModel);
            shoppingCart.getItems().add(this.cartItemService.saveItem(item));
        } else {
            item.setQuantity(item.getQuantity() + cartItemAddBindModel.getQuantity());
            this.cartItemService.saveItem(item);
        }


        shoppingCart.setTotalCartAmount(shoppingCart.getItems()
                .stream()
                .mapToDouble(CartItemServiceModel::getSubTotal)
                .sum());

        this.shoppingCartRepository.save(this.modelMapper.map(shoppingCart, ShoppingCart.class));
        this.productService.decreaseProductQty(item.getItem(), item.getQuantity());
    }

    @Override
    public void removeItemFromCart(String itemId, String username) {
        ShoppingCartServiceModel shoppingCart = this.modelMapper
                .map(this.userService.getUserByUsername(username), UserServiceModel.class).getShoppingCart();

        CartItemServiceModel cartItemServiceModel = this.cartItemService.getItemById(itemId);

        for (int i = 0; i < shoppingCart.getItems().size(); i++) {
            if (shoppingCart.getItems().get(i).getItem().getId().equals(cartItemServiceModel.getItem().getId())) {
                shoppingCart.getItems().remove(i);
                break;
            }
        }

        shoppingCart.setTotalCartAmount(shoppingCart.getItems()
                .stream()
                .mapToDouble(CartItemServiceModel::getSubTotal).sum());

        this.shoppingCartRepository.save(this.modelMapper.map(shoppingCart, ShoppingCart.class));
        this.cartItemService.deleteItem(cartItemServiceModel.getId());
        this.productService.increaseProductQuantity(cartItemServiceModel.getItem(), cartItemServiceModel.getQuantity());
    }

    @Override
    public void retrieveShoppingCart(ShoppingCartServiceModel shoppingCart) {
        shoppingCart.getItems()
                .forEach(ci -> this.cartItemService.deleteItem(ci.getId()));

        shoppingCart.setItems(new ArrayList<>());
        shoppingCart.setTotalCartAmount(0.0);
        this.shoppingCartRepository.save(this.modelMapper.map(shoppingCart, ShoppingCart.class));
    }

}
