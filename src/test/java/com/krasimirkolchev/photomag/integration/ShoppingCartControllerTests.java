package com.krasimirkolchev.photomag.integration;

import com.krasimirkolchev.photomag.models.entities.User;
import com.krasimirkolchev.photomag.repositories.ProductRepository;
import com.krasimirkolchev.photomag.repositories.ShoppingCartRepository;
import com.krasimirkolchev.photomag.repositories.UserRepository;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.security.Principal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ShoppingCartControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext wac;
    @Autowired
    private ShoppingCartRepository repository;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
        repository.deleteAll();

        User user = new User();
        user.setUsername("admin");
        user.setEmail("asd@asd.as");
        user.setFirstName("admin");
        user.setLastName("adminov");
        user.setPassword("password");
        user.setProfilePhoto("file");
        userRepository.save(user);


    }

    @Test
    @WithMockUser(username = "admin", roles = {"USER", "ADMIN"})
    public void shoppingCartReturnsCorrectView() throws Exception {
        Principal principal = Mockito.mock(Principal.class);
        Mockito.when(principal.getName()).thenReturn("admin");

        this.mockMvc.perform(get("/shopping-cart"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("shopping-cart"));
    }

}
