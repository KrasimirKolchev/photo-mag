package com.krasimirkolchev.photomag.integration;

import com.krasimirkolchev.photomag.models.bindingModels.ProductAddBindingModel;
import com.krasimirkolchev.photomag.models.entities.Brand;
import com.krasimirkolchev.photomag.models.entities.ProductCategory;
import com.krasimirkolchev.photomag.models.serviceModels.BrandServiceModel;
import com.krasimirkolchev.photomag.repositories.BrandRepository;
import com.krasimirkolchev.photomag.repositories.ProductCategoryRepository;
import com.krasimirkolchev.photomag.repositories.ProductRepository;
import com.krasimirkolchev.photomag.services.impl.BrandServiceImpl;
import com.krasimirkolchev.photomag.web.controllers.ProductController;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext wac;
    @Autowired
    private ProductRepository repository;
    @Autowired
    private ProductController controller;
    @Autowired
    private ProductCategoryRepository categoryRepository;
    @Autowired
    private BrandServiceImpl brandService;
    @Autowired
    private BrandRepository brandRepository;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
        repository.deleteAll();
    }

    @Test
    @WithMockUser(username = "admin", roles = {"USER", "ADMIN"})
    public void addProductReturnsCorrectView() throws Exception {
        this.mockMvc.perform(get("/products/add"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("add-product"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ROOT_ADMIN", "ADMIN"})
    public void addProductConfSavesTheProductInDB() throws IOException {
        File file2 = new File("src/main/resources/static/img/user-picture.jpg");
        MockMultipartFile file = new MockMultipartFile("user-picture", "user-picture.jpg",
                MediaType.IMAGE_JPEG_VALUE, FileCopyUtils.copyToByteArray(file2));

        brandService = Mockito.mock(BrandServiceImpl.class);

        ProductCategory cat = categoryRepository.findProductCategoryByNameLike("Accessories");
        brandRepository = Mockito.mock(BrandRepository.class);
        Mockito.when(brandRepository.existsByName(anyString())).thenReturn(true);

        BrandServiceModel brand = new BrandServiceModel();
        brand.setId("123");
        brand.setName("Canon");

        Mockito.when(brandRepository.findById(anyString())).thenReturn(Optional.of(any()));
        Mockito.when(brandService.getBrandById(anyString())).thenReturn(brand);

        RedirectAttributes attributes = null;

        ProductAddBindingModel product = new ProductAddBindingModel();
        product.setName("Item 12341234");
        product.setDescription("asdasdasdasdasdasdasdasdasd");
        product.setPrice(11.00);
        product.setQuantity(5);
        product.setProductCategory(cat.getId());
        product.setBrand("123");
        product.setProductGallery(List.of(file));

        BindingResult result = new BeanPropertyBindingResult(product, "productAddBindingModel");

        controller.addProductConf(product, result, attributes);

        Assert.assertEquals(1, repository.count());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"USER", "ADMIN"})
    public void allProductsByCategoryReturnsCorrectView() throws Exception {
        this.mockMvc.perform(get("/products/{categoryName}", "DSLR Cameras"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("products"));
    }



}
