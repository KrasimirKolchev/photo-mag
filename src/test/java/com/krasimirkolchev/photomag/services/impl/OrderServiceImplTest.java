package com.krasimirkolchev.photomag.services.impl;

import com.krasimirkolchev.photomag.models.entities.*;
import com.krasimirkolchev.photomag.models.serviceModels.*;
import com.krasimirkolchev.photomag.repositories.OrderRepository;
import com.krasimirkolchev.photomag.services.*;
import com.stripe.model.Charge;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import static org.mockito.ArgumentMatchers.*;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
class OrderServiceImplTest {
    private OrderServiceImpl orderService;
    private OrderRepository orderRepository;
    private UserServiceImpl userService;
    private ShoppingCartServiceImpl shoppingCartService;
    private OrderItemServiceImpl orderItemService;
    private ProductServiceImpl productService;
    private AddressServiceImpl addressService;
    private ModelMapper modelMapper;
    private List<OrderItemServiceModel> orderItems = new ArrayList<>();
    private Order orderEntity;

    @BeforeEach
    void setup() {
        orderRepository = Mockito.mock(OrderRepository.class);
        userService = Mockito.mock(UserServiceImpl.class);
        shoppingCartService = Mockito.mock(ShoppingCartServiceImpl.class);
        productService = Mockito.mock(ProductServiceImpl.class);
        addressService = Mockito.mock(AddressServiceImpl.class);
        modelMapper = Mockito.mock(ModelMapper.class);
        orderItemService = Mockito.mock(OrderItemServiceImpl.class);

        orderService = new OrderServiceImpl(orderRepository, userService, shoppingCartService, orderItemService,
                productService, addressService, modelMapper);

        ProductServiceModel product1 = new ProductServiceModel(){{
            setName("Product 123456");
            setPrice(3.00);
            setDescription("asdasdasdasdasdasda");
            setMainPhoto("mainphoto.jpg");
            setBrand(new BrandServiceModel(){{setName("Canon");}});
            setId("123");
        }};

        OrderItemServiceModel item1 = new OrderItemServiceModel(){{
            setOrderItem(product1);
            setQuantity(1);
            setSubTotal(3.00);
        }};

        ProductServiceModel product2 = new ProductServiceModel(){{
            setName("Product 654321");
            setPrice(2.00);
            setDescription("asdasdasdasdasdasda");
            setMainPhoto("mainphoto.jpg");
            setBrand(new BrandServiceModel(){{setName("Nikon");}});
            setId("321");
        }};

        OrderItemServiceModel item2 = new OrderItemServiceModel(){{
            setOrderItem(product2);
            setQuantity(1);
            setSubTotal(2.00);
        }};

        orderEntity = new Order(){{
            setId("asd123");
            setPurchaseDateTime(LocalDateTime.now());
            setUser(new User());
            setAddress(new Address());
            setChargeId("456987");
            setOrderItems(List.of(new OrderItems(), new OrderItems()));
            setTotalAmount(123.00);
        }};

        orderItems = List.of(item1, item2);

        ModelMapper mapper = new ModelMapper();

        Mockito.when(modelMapper.map(any(OrderServiceModel.class), eq(Order.class)))
                .thenAnswer(invocation -> mapper.map(invocation.getArguments()[0], Order.class));

        Mockito.when(modelMapper.map(any(Order.class), eq(OrderServiceModel.class)))
                .thenAnswer(invocation -> mapper.map(invocation.getArguments()[0], OrderServiceModel.class));

        Mockito.when(modelMapper.map(any(OrderItemServiceModel.class), eq(OrderItems.class)))
                .thenAnswer(invocation -> mapper.map(invocation.getArguments()[0], OrderItems.class));

        Mockito.when(modelMapper.map(any(OrderItems.class), eq(OrderItemServiceModel.class)))
                .thenAnswer(invocation -> mapper.map(invocation.getArguments()[0], OrderItemServiceModel.class));

        Mockito.when(modelMapper.map(any(ProductServiceModel.class), eq(Product.class)))
                .thenAnswer(invocation -> mapper.map(invocation.getArguments()[0], Product.class));

        Mockito.when(modelMapper.map(any(Product.class), eq(ProductServiceModel.class)))
                .thenAnswer(invocation -> mapper.map(invocation.getArguments()[0], ProductServiceModel.class));

        Mockito.when(modelMapper.map(any(UserServiceModel.class), eq(User.class)))
                .thenAnswer(invocation -> mapper.map(invocation.getArguments()[0], User.class));

        Mockito.when(modelMapper.map(any(User.class), eq(UserServiceModel.class)))
                .thenAnswer(invocation -> mapper.map(invocation.getArguments()[0], UserServiceModel.class));

        Mockito.when(modelMapper.map(any(AddressServiceModel.class), eq(Address.class)))
                .thenAnswer(invocation -> mapper.map(invocation.getArguments()[0], Address.class));

        Mockito.when(modelMapper.map(any(Address.class), eq(AddressServiceModel.class)))
                .thenAnswer(invocation -> mapper.map(invocation.getArguments()[0], AddressServiceModel.class));
    }

    @Test
    void getAllOrders() {
        Mockito.when(orderRepository.findAll()).thenReturn(List.of(orderEntity));

        List<OrderServiceModel> exp = orderService.getAllOrders();

        assertEquals(exp.size(), 1);
        assertEquals(exp.get(0).getId(), orderEntity.getId());
        assertEquals(exp.get(0).getOrderItems().size(), 2);
    }

    @Test
    void getAllOrdersByUsernameIfValidReturnsOrders() {
        Mockito.when(orderRepository.getOrdersByUser_UsernameOrderByPurchaseDateTime(anyString())).thenReturn(List.of(orderEntity));

        List<OrderServiceModel> exp = orderService.getAllOrdersByUsername("User");

        assertEquals(exp.size(), 1);
        assertEquals(exp.get(0).getChargeId(), orderEntity.getChargeId());
    }

    @Test
    void createOrderSaveTheOrder() {
        Mockito.when(orderRepository.save(any())).thenReturn(orderEntity);

        OrderServiceModel exp = orderService.createOrder(new OrderServiceModel());

        assertEquals(exp.getChargeId(), orderEntity.getChargeId());
        assertEquals(exp.getId(), orderEntity.getId());
    }

    @Test
    void generateOrderShouldCreate() {
        OrderServiceModel act = new OrderServiceModel(){{
            setOrderItems(orderItems);
            setTotalAmount(456.01);
            setUser(new UserServiceModel());
            setChargeId("123asddsa321");
            setPurchaseDateTime(LocalDateTime.now());
            setAddress(new AddressServiceModel());
            setId("987654");
        }};

        Charge charge = Mockito.mock(Charge.class);

        Principal pr = Mockito.mock(Principal.class);
        Mockito.when(pr.getName()).thenReturn("User");
//        Mockito.when(userService.getUserByUsername(anyString())).thenReturn(new User());

        Mockito.when(orderService.generateOrder(any(), any(), anyString())).thenReturn(act);

        OrderServiceModel exp = orderService.generateOrder(charge, pr, "addressId");

        assertEquals(exp.getId(), act.getId());
    }
}