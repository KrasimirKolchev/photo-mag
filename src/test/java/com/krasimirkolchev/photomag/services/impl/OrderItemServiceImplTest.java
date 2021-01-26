package com.krasimirkolchev.photomag.services.impl;

import com.krasimirkolchev.photomag.models.entities.Brand;
import com.krasimirkolchev.photomag.models.entities.CartItem;
import com.krasimirkolchev.photomag.models.entities.OrderItems;
import com.krasimirkolchev.photomag.models.entities.Product;
import com.krasimirkolchev.photomag.models.serviceModels.BrandServiceModel;
import com.krasimirkolchev.photomag.models.serviceModels.CartItemServiceModel;
import com.krasimirkolchev.photomag.models.serviceModels.OrderItemServiceModel;
import com.krasimirkolchev.photomag.models.serviceModels.ProductServiceModel;
import com.krasimirkolchev.photomag.repositories.OrderItemRepository;
import com.krasimirkolchev.photomag.services.OrderItemService;
import com.stripe.model.OrderItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;

import static org.mockito.ArgumentMatchers.*;

import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
class OrderItemServiceImplTest {
    private OrderItemServiceImpl orderItemService;
    private OrderItemRepository repository;
    private ModelMapper modelMapper;
    private OrderItemServiceModel item;


    @BeforeEach
    void setUp() {
        modelMapper = Mockito.mock(ModelMapper.class);
        ProductServiceModel product = new ProductServiceModel() {{
            setName("Product 123456");
            setPrice(3.00);
            setQuantity(1);
            setDescription("asdasdasdasdasdasda");
            setMainPhoto("mainphoto.jpg");
            setBrand(new BrandServiceModel() {{
                setName("Canon");
            }});
            setId("123");
        }};
        item = new OrderItemServiceModel() {{
            setOrderItem(product);
            setSubTotal(3.00);
            setQuantity(1);
        }};

        repository = Mockito.mock(OrderItemRepository.class);
        orderItemService = new OrderItemServiceImpl(repository, modelMapper);

        ModelMapper mapper = new ModelMapper();

        Mockito.when(modelMapper.map(any(OrderItemServiceModel.class), eq(OrderItems.class)))
                .thenAnswer(invocation -> mapper.map(invocation.getArguments()[0], OrderItems.class));

        Mockito.when(modelMapper.map(any(OrderItems.class), eq(OrderItemServiceModel.class)))
                .thenAnswer(invocation -> mapper.map(invocation.getArguments()[0], OrderItemServiceModel.class));

        Mockito.when(modelMapper.map(any(ProductServiceModel.class), eq(Product.class)))
                .thenAnswer(invocation -> mapper.map(invocation.getArguments()[0], Product.class));

        Mockito.when(modelMapper.map(any(Product.class), eq(ProductServiceModel.class)))
                .thenAnswer(invocation -> mapper.map(invocation.getArguments()[0], ProductServiceModel.class));
    }

    @Test
    void addOrderItemsShouldSaveItem() {
        orderItemService.addOrderItem(item);

        Mockito.verify(repository).save(any());
    }

    @Test
    void addOrderItemsShouldReturnItem() {
        Mockito.when(repository.save(any())).thenReturn(any());
        Mockito.when(orderItemService.addOrderItem(any())).thenReturn(item);
        OrderItemServiceModel exp = orderItemService.addOrderItem(item);

        assertEquals(exp.getOrderItem().getName(), item.getOrderItem().getName());
    }
}