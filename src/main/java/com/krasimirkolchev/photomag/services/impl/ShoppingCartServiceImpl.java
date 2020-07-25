package com.krasimirkolchev.photomag.services.impl;

import com.krasimirkolchev.photomag.models.bindingModels.CartItemAddBindModel;
import com.krasimirkolchev.photomag.models.entities.CartItem;
import com.krasimirkolchev.photomag.models.entities.Product;
import com.krasimirkolchev.photomag.models.entities.ShoppingCart;
import com.krasimirkolchev.photomag.models.entities.User;
import com.krasimirkolchev.photomag.models.serviceModels.UserServiceModel;
import com.krasimirkolchev.photomag.repositories.ShoppingCartRepository;
import com.krasimirkolchev.photomag.services.CartItemService;
import com.krasimirkolchev.photomag.services.ProductService;
import com.krasimirkolchev.photomag.services.ShoppingCartService;
import com.krasimirkolchev.photomag.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

        ShoppingCart shoppingCart = null;

        if (user.getShoppingCart() == null) {
            user.setShoppingCart( new ShoppingCart());
        }
            shoppingCart = user.getShoppingCart();


        CartItem cartItem = this.modelMapper.map(cartItemAddBindModel, CartItem.class);
        cartItem.setItem(this.modelMapper.map(this.productService.getProductById(cartItemAddBindModel.getId()), Product.class));

        if (shoppingCart.getCartItem().size() > 0) {
            for (int i = 0; i < shoppingCart.getCartItem().size(); i++) {
                if (shoppingCart.getCartItem().get(i).getItem().getId().equals(cartItem.getItem().getId())) {
                    CartItem item = shoppingCart.getCartItem().get(i);
                    item.setQuantity(shoppingCart.getCartItem().get(i).getQuantity() + cartItem.getQuantity());
                    this.cartItemService.saveItem(item);
                    break;
                }
            }
        } else {
            shoppingCart.getCartItem().add(this.cartItemService.saveItem(cartItem));
        }
        shoppingCart.setTotalCartAmount(shoppingCart.getCartItem().stream().mapToDouble(CartItem::getSubTotal).sum());

        this.shoppingCartRepository.save(shoppingCart);
        this.userService.saveUser(this.modelMapper.map(user, User.class));
    }


}
