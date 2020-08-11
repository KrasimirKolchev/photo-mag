package com.krasimirkolchev.photomag.services.impl;

import com.krasimirkolchev.photomag.models.entities.Brand;
import com.krasimirkolchev.photomag.models.entities.CartItem;
import com.krasimirkolchev.photomag.models.entities.Product;
import com.krasimirkolchev.photomag.models.serviceModels.BrandServiceModel;
import com.krasimirkolchev.photomag.models.serviceModels.CartItemServiceModel;
import com.krasimirkolchev.photomag.models.serviceModels.ProductServiceModel;
import com.krasimirkolchev.photomag.repositories.CartItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
class CartItemServiceImplTest {
    private CartItemServiceImpl cartItemService;
    private CartItemRepository cartItemRepository;
    private ModelMapper modelMapper;
    private List<CartItem> cartItems;
    private CartItem item;

    @BeforeEach
    void setUp() {
        modelMapper = Mockito.mock(ModelMapper.class);
        Product product = new Product(){{
            setName("Product 123456");
            setPrice(3.00);
            setQuantity(1);
            setDescription("asdasdasdasdasdasda");
            setMainPhoto("mainphoto.jpg");
            setBrand(new Brand(){{setName("Canon");}});
            setId("123");
        }};
        item = new CartItem(){{
            setItem(product);
            setSubTotal(3.00);
            setQuantity(1);
        }};

        cartItems = List.of(item);
        cartItemRepository = Mockito.mock(CartItemRepository.class);
        cartItemService = new CartItemServiceImpl(cartItemRepository, modelMapper);
        Mockito.when(cartItemRepository.getOne(anyString())).thenReturn(item);


        ModelMapper mapper = new ModelMapper();

        Mockito.when(modelMapper.map(any(CartItemServiceModel.class), eq(CartItem.class)))
                .thenAnswer(invocation -> mapper.map(invocation.getArguments()[0], CartItem.class));

        Mockito.when(modelMapper.map(any(CartItem.class), eq(CartItemServiceModel.class)))
                .thenAnswer(invocation -> mapper.map(invocation.getArguments()[0], CartItemServiceModel.class));

//        Mockito.when(modelMapper.map(any(BrandServiceModel.class), eq(Brand.class)))
//                .thenAnswer(invocation -> mapper.map(invocation.getArguments()[0], Brand.class));
//
//        Mockito.when(modelMapper.map(any(Brand.class), eq(BrandServiceModel.class)))
//                .thenAnswer(invocation -> mapper.map(invocation.getArguments()[0], BrandServiceModel.class));

        Mockito.when(modelMapper.map(any(ProductServiceModel.class), eq(Product.class)))
                .thenAnswer(invocation -> mapper.map(invocation.getArguments()[0], Product.class));

        Mockito.when(modelMapper.map(any(Product.class), eq(ProductServiceModel.class)))
                .thenAnswer(invocation -> mapper.map(invocation.getArguments()[0], ProductServiceModel.class));
    }

    @Test
    void saveItemSaveTheItem() {
        ProductServiceModel prod = new ProductServiceModel(){{
            setName("Product 12345678");
            setPrice(12.00);
            setDescription("asdasdasdasdasdasda");
            setMainPhoto("mainphoto.jpg");
            setBrand(new BrandServiceModel(){{setName("Canon"); setId("4321");}});
            setId("123");
        }};
        CartItemServiceModel act = new CartItemServiceModel(){{
            setItem(prod);
            setQuantity(1);
            setId("111");
            setSubTotal(24.00);
        }};

        cartItemService.saveItem(act);
        Mockito.verify(cartItemRepository).save(any());
    }

    @Test
    void saveItemShouldReturnTheItem() {
        ProductServiceModel prod = new ProductServiceModel(){{
            setName("Product 12345678");
            setPrice(12.00);
            setDescription("asdasdasdasdasdasda");
            setMainPhoto("mainphoto.jpg");
            setBrand(new BrandServiceModel(){{setName("Canon"); setId("4321");}});
            setId("123");
        }};
        CartItemServiceModel act = new CartItemServiceModel(){{
            setItem(prod);
            setQuantity(1);
            setId("111");
            setSubTotal(24.00);
        }};

        Mockito.when(cartItemRepository.save(any())).thenReturn(any());
        Mockito.when(cartItemService.saveItem(any())).thenReturn(act);

        CartItemServiceModel exp = cartItemService.saveItem(act);

        assertEquals(exp.getItem().getName(), act.getItem().getName());
    }

    @Test
    void deleteItemByIdShouldDeleteTheCartItem() {
        cartItemService.deleteItem("123");

        Mockito.verify(cartItemRepository).deleteById(any());
    }

    @Test
    void getItemByIdReturnsTheItem() {
        CartItemServiceModel exp = cartItemService.getItemById("1");

        assertEquals(exp.getSubTotal(), item.getSubTotal());
        assertEquals(exp.getQuantity(), item.getQuantity());
        assertEquals(exp.getItem().getName(), item.getItem().getName());
        assertEquals(exp.getItem().getId(), item.getItem().getId());
    }
}