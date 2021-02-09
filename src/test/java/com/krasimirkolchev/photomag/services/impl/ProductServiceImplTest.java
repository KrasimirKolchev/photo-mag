package com.krasimirkolchev.photomag.services.impl;

import com.krasimirkolchev.photomag.error.ProductNotFoundException;
import com.krasimirkolchev.photomag.models.entities.Brand;
import com.krasimirkolchev.photomag.models.entities.Product;
import com.krasimirkolchev.photomag.models.entities.ProductCategory;
import com.krasimirkolchev.photomag.models.serviceModels.BrandServiceModel;
import com.krasimirkolchev.photomag.models.serviceModels.ProductCategoryServiceModel;
import com.krasimirkolchev.photomag.models.serviceModels.ProductServiceModel;
import com.krasimirkolchev.photomag.repositories.ProductRepository;
import com.krasimirkolchev.photomag.services.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;

class ProductServiceImplTest {
    private ProductServiceImpl service;
    private ProductRepository repository;
    private CloudinaryServiceImpl cloudinaryService;
    private ModelMapper modelMapper;
    private TestEntityManager entityManager;

    private MultipartFile file;

    @BeforeEach
    void setUp() throws IOException {
        repository = Mockito.mock(ProductRepository.class);
        cloudinaryService = Mockito.mock(CloudinaryServiceImpl.class);
        modelMapper = Mockito.mock(ModelMapper.class);
        service = new ProductServiceImpl(repository, modelMapper, cloudinaryService);

        ByteArrayInputStream content = new ByteArrayInputStream("content".getBytes());
        file = new MockMultipartFile("file", "file.jpg", "image/jpeg", content);

        ModelMapper mapper = new ModelMapper();

        Mockito.when(modelMapper.map(any(ProductServiceModel.class), eq(Product.class)))
                .thenAnswer(invocation -> mapper.map(invocation.getArguments()[0], Product.class));

        Mockito.when(modelMapper.map(any(Product.class), eq(ProductServiceModel.class)))
                .thenAnswer(invocation -> mapper.map(invocation.getArguments()[0], ProductServiceModel.class));

        Mockito.when(modelMapper.map(any(ProductCategoryServiceModel.class), eq(ProductCategory.class)))
                .thenAnswer(invocation -> mapper.map(invocation.getArguments()[0], ProductCategory.class));

        Mockito.when(modelMapper.map(any(ProductCategory.class), eq(ProductCategoryServiceModel.class)))
                .thenAnswer(invocation -> mapper.map(invocation.getArguments()[0], ProductCategoryServiceModel.class));

        Mockito.when(modelMapper.map(any(BrandServiceModel.class), eq(Brand.class)))
                .thenAnswer(invocation -> mapper.map(invocation.getArguments()[0], Brand.class));

        Mockito.when(modelMapper.map(any(Brand.class), eq(BrandServiceModel.class)))
                .thenAnswer(invocation -> mapper.map(invocation.getArguments()[0], BrandServiceModel.class));
    }

    @Test
    void addProduct() throws IOException {
        Product p11 = new Product() {{
            setBrand(new Brand() {{
                setName("a");
            }});
            setQuantity(12);
            setDescription("asd");
            setName("item");
            setPrice(12.01);
            setProductCategory(new ProductCategory() {{
                setName("aa");
            }});
            setMainPhoto("asd");
            setProductGallery(new ArrayList<>());
        }};

        Mockito.when(repository.save(any())).thenReturn(p11);

        ProductServiceModel prod = service.addProduct(any(), anyList());

        assertEquals(prod.getName(), p11.getName());
    }

    @Test
    void getProductsByCategoryName() {
        Product p1 = new Product() {{
            setId("1");
            setBrand(new Brand());
            setMainPhoto("photo");
            setDescription("aaaaaaaaaaaaaaaaaaaaaaaaaaaa");
            setQuantity(3);
            setPrice(12.00);
            setName("Item 12341234");
            setProductCategory(new ProductCategory() {{
                setName("a");
            }});
            setProductGallery(new ArrayList<>());
            setDeleted(false);
        }};

        Mockito.when(repository
                .getAllByProductCategory_NameAndQuantityIsGreaterThanAndDeletedFalse("a", 0))
                .thenReturn(List.of(p1));
        List<ProductServiceModel> products = service.getProductsByCategoryName("a");

        assertEquals(1, products.size());

    }

    @Test
    void getProductByIdReturnProductIfExist() {
        Product p1 = new Product() {{
            setId("1");
            setBrand(new Brand() {{
                setName("a");
            }});
            setMainPhoto("photo");
            setDescription("aaaaaaaaaaaaaaaaaaaaaaaaaaaa");
            setQuantity(3);
            setPrice(12.00);
            setName("Item 12341234");
            setProductCategory(new ProductCategory());
            setProductGallery(new ArrayList<>());
            setDeleted(false);
        }};
        Mockito.when(repository.findById(anyString())).thenReturn(Optional.of(p1));

        ProductServiceModel prod = service.getProductById("1");
        assertEquals(p1.getId(), prod.getId());
        assertEquals(p1.getName(), prod.getName());
    }

    @Test
    void getProductByIdThrowsIfNotExist() {
        Mockito.when(repository.findById(anyString())).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> this.service.getProductById("2"));
    }

    @Test
    void decreaseProductQtyShouldDecrease() {
        ProductServiceModel p11 = new ProductServiceModel() {{
            setBrand(new BrandServiceModel() {{
                setName("a");
            }});
            setQuantity(12);
            setDescription("asd");
            setName("item");
            setPrice(12.01);
            setProductCategory(new ProductCategoryServiceModel() {{
                setName("aa");
            }});
            setMainPhoto("asd");
            setProductGallery(new ArrayList<>());
        }};
        ProductService service1 = Mockito.mock(ProductService.class);
        Mockito.doNothing().when(service1).decreaseProductQty(isA(ProductServiceModel.class), isA(Integer.class));

        service1.decreaseProductQty(p11, 1);

        Mockito.verify(service1, Mockito.times(1)).decreaseProductQty(p11, 1);
    }

    @Test
    void increaseProductQuantityShouldIncrease() {
        ProductServiceModel p11 = new ProductServiceModel() {{
            setBrand(new BrandServiceModel() {{
                setName("a");
            }});
            setQuantity(12);
            setDescription("asd");
            setName("item");
            setPrice(12.01);
            setProductCategory(new ProductCategoryServiceModel() {{
                setName("aa");
            }});
            setMainPhoto("asd");
            setProductGallery(new ArrayList<>());
        }};
        ProductService service1 = Mockito.mock(ProductService.class);
        Mockito.doNothing().when(service1).increaseProductQuantity(isA(ProductServiceModel.class), isA(Integer.class));

        service1.increaseProductQuantity(p11, 1);

        Mockito.verify(service1, Mockito.times(1)).increaseProductQuantity(p11, 1);
    }

    @Test
    void editProduct() {
        Product p1 = new Product() {{
            setId("1");
            setBrand(new Brand() {{
                setName("a");
            }});
            setMainPhoto("photo");
            setDescription("aaaaaaaaaaaaaaaaaaaaaaaaaaaa");
            setQuantity(3);
            setPrice(12.00);
            setName("Item 12341234");
            setProductCategory(new ProductCategory());
            setProductGallery(new ArrayList<>());
            setDeleted(false);
        }};
        Mockito.when(repository.findById(anyString())).thenReturn(Optional.of(p1));

        ProductServiceModel edited = new ProductServiceModel();

        edited.setDescription("description");
        edited.setQuantity(5);
        edited.setPrice(5.05);
        edited.setName("Item 12341234");

        ProductServiceModel newProduct = service.editProduct("1", edited);

        assertEquals(5.05, newProduct.getPrice());
        assertEquals(5, newProduct.getQuantity());
        assertEquals("description", newProduct.getDescription());
    }

    @Test
    void deleteProductShouldSetDeletedIsTrue() {
        ProductService service1 = Mockito.mock(ProductService.class);
        Mockito.doNothing().when(service1).deleteProduct(isA(String.class));

        service1.deleteProduct("asd");

        Mockito.verify(service1, Mockito.times(1)).deleteProduct("asd");
    }

    @Test
    void getAllProductsReturnProducts() {
        Product p1 = new Product() {{
            setId("1");
            setBrand(new Brand() {{
                setName("a");
            }});
            setMainPhoto("photo");
            setDescription("aaaaaaaaaaaaaaaaaaaaaaaaaaaa");
            setQuantity(3);
            setPrice(12.00);
            setName("Item 12341234");
            setProductCategory(new ProductCategory());
            setProductGallery(new ArrayList<>());
            setDeleted(false);
        }};
        Product p2 = new Product() {{
            setId("2");
            setBrand(new Brand() {{
                setName("b");
            }});
            setMainPhoto("photo2");
            setDescription("2aaaaaaaaaaaaaaaaaaaaaaaaaaaa");
            setQuantity(2);
            setPrice(10.00);
            setName("Item2 12341234");
            setProductCategory(new ProductCategory());
            setProductGallery(new ArrayList<>());
            setDeleted(false);
        }};

        Mockito.when(repository.getAllByQuantityIsGreaterThanOrderByBrandNameAsc(0)).thenReturn(List.of(p1, p2));

        List<ProductServiceModel> act = service.getAllActiveProducts();

        assertEquals(act.size(), 2);
        assertEquals(act.get(0).getBrand().getName(), "a");
    }

}