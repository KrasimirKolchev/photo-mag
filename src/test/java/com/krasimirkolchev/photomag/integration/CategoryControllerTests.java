package com.krasimirkolchev.photomag.integration;

import com.krasimirkolchev.photomag.models.bindingModels.CategoryAddBindingModel;
import com.krasimirkolchev.photomag.models.serviceModels.ProductCategoryServiceModel;
import com.krasimirkolchev.photomag.repositories.ProductCategoryRepository;
import com.krasimirkolchev.photomag.web.controllers.CategoryController;
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
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.util.ArrayList;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class CategoryControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext wac;
    @Autowired
    private ProductCategoryRepository repository;
    @Autowired
    private CategoryController controller;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
        repository.deleteAll();
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ROOT_ADMIN", "ADMIN"})
    public void allCategoriesShouldReturnCorrectView() throws Exception {
        this.mockMvc.perform(get("/categories"))
                .andExpect(status().isOk())
                .andExpect(view().name("categories"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ROOT_ADMIN", "ADMIN"})
    public void addCategoryShouldReturnCorrectView() throws Exception {
        this.mockMvc.perform(get("/categories/add"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("add-category"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ROOT_ADMIN", "ADMIN"})
    public void addCategoryConfWhenModelValidSaveInDB() throws Exception {
        File file2 = new File("src/main/resources/static/img/user-picture.jpg");
        MockMultipartFile file = new MockMultipartFile("user-picture", "user-picture.jpg",
                MediaType.IMAGE_JPEG_VALUE, FileCopyUtils.copyToByteArray(file2));

        RedirectAttributes attributes = null;

        CategoryAddBindingModel model = new CategoryAddBindingModel();
        model.setName("Test category");
        model.setPhoto(file);
        BindingResult result = new BeanPropertyBindingResult(model, "categoryAddBindingModel");

        controller.addCategoryConf(model, result, attributes);

        Assert.assertEquals(7, repository.count());
    }

    //initialized categories plus one added
    @Test
    @WithMockUser(username = "admin", roles = {"ROOT_ADMIN", "ADMIN"})
    public void addCategoryConfWhenModelInvalidRedirectsAndNotSave() throws Exception {
        this.mockMvc.perform(post("/categories/add")
                .param("name", "test name")
        )
                .andExpect(status().is3xxRedirection());

        Assert.assertEquals(7, repository.count());
    }


    //initialized categories plus one added
    @Test
    @WithMockUser(username = "admin", roles = {"ROOT_ADMIN", "ADMIN"})
    public void allCategoriesShouldReturnInitializedCategories() throws Exception {
        MvcResult res = this.mockMvc.perform(get("/categories")).andReturn();
        ArrayList<ProductCategoryServiceModel> cat = (ArrayList<ProductCategoryServiceModel>) res.getModelAndView().getModel().get("categories");
        Assert.assertEquals(7, cat.size());
    }
}
