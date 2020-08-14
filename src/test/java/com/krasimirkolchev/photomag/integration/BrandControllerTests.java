package com.krasimirkolchev.photomag.integration;

import com.krasimirkolchev.photomag.repositories.BrandRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.security.Principal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class BrandControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext wac;
    @Autowired
    private BrandRepository repository;

    @BeforeEach
    public void init(){
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
        repository.deleteAll();
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ROOT_ADMIN", "ADMIN"})
    public void addBrandReturnsCorrectView() throws Exception {
        this.mockMvc.perform(get("/brands/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("add-brand"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ROOT_ADMIN", "ADMIN"})
    public void addBrandConfWhenModelInvalidShouldNotSaveInDB() throws Exception {
        Principal mockPrincipal = Mockito.mock(Principal.class);
        Mockito.when(mockPrincipal.getName()).thenReturn("admin");

        mockMvc.perform(post("/brands/add")
                .param("name", "")
        )
                .andExpect(status().is3xxRedirection());

        Assert.assertEquals(0, repository.count());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ROOT_ADMIN", "ADMIN"})
    public void addBrandConfWhenModelValidSaveInDB() throws Exception {
        Principal mockPrincipal = Mockito.mock(Principal.class);
        Mockito.when(mockPrincipal.getName()).thenReturn("admin");

        mockMvc.perform(post("/brands/add")
                .param("name", "Canon")
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/home"));

        Assert.assertEquals(1, repository.count());
    }


}
