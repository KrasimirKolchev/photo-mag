package com.krasimirkolchev.photomag.integration;

import com.krasimirkolchev.photomag.models.entities.*;
import com.krasimirkolchev.photomag.repositories.OrderRepository;
import com.krasimirkolchev.photomag.repositories.ProductRepository;
import com.krasimirkolchev.photomag.repositories.UserRepository;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithSecurityContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class OrderControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext wac;
    @Autowired
    private OrderRepository repository;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
        repository.deleteAll();

        User user = new User();
        user.setUsername("admin");
        userRepository.save(user);

        Order order = new Order();
        order.setChargeId("asd123");
        order.setAddress(new Address() {{
            setId("123");
            setStreet("str");
            setCountry("country");
            setCity("city");
        }});
        order.setUser(new User());
        order.setPurchaseDateTime(LocalDateTime.now());
        order.setTotalAmount(123.22);
        order.setOrderItems(List.of(new OrderItems() {{
            setId("321");
            setSubTotal(123.22);
            setQuantity(1);
            setOrderItem(new Product() {{
                setId("222");
            }});
        }}));
        repository.save(order);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ROOT_ADMIN", "ADMIN"})
    public void allOrdersShouldReturnCorrectView() throws Exception {

        this.mockMvc.perform(get("/orders/all"))
                .andExpect(status().isOk())
                .andExpect(view().name("orders-all"));
    }


}
