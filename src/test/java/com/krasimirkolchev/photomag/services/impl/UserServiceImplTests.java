package com.krasimirkolchev.photomag.services.impl;

import com.krasimirkolchev.photomag.error.UserNotFoundException;
import com.krasimirkolchev.photomag.models.entities.Address;
import com.krasimirkolchev.photomag.models.entities.Role;
import com.krasimirkolchev.photomag.models.entities.ShoppingCart;
import com.krasimirkolchev.photomag.models.entities.User;
import com.krasimirkolchev.photomag.models.serviceModels.RoleServiceModel;
import com.krasimirkolchev.photomag.models.serviceModels.UserServiceModel;
import com.krasimirkolchev.photomag.repositories.RoleRepository;
import com.krasimirkolchev.photomag.repositories.UserRepository;
import com.krasimirkolchev.photomag.services.RoleService;
import com.krasimirkolchev.photomag.services.UserService;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
class UserServiceImplTests {
    @InjectMocks
    UserServiceImpl userService;
    @MockBean
    UserRepository userRepository;
    @MockBean
    RoleService roleService;
    @MockBean
    RoleRepository roleRepository;
    @MockBean
    AddressServiceImpl addressService;
    @MockBean
    CloudinaryServiceImpl cloudinaryService;
    @MockBean
    BCryptPasswordEncoder encoder;
    @MockBean
    private ModelMapper modelMapper;

    private User user;
    private MultipartFile file;

    @BeforeEach
    protected void setup() throws IOException {
        MockitoAnnotations.initMocks(this);
        userService = new UserServiceImpl(userRepository, roleService, addressService, cloudinaryService, encoder, modelMapper);
        ModelMapper mapper = new ModelMapper();
        BCryptPasswordEncoder actualEncoder = new BCryptPasswordEncoder();


        ByteArrayInputStream content = new ByteArrayInputStream("content".getBytes());
        file = new MockMultipartFile("file", "file.jpg", "image/jpeg", content);

        Role ROLE_USER = new Role("ROLE_USER");
        Role ROLE_ADMIN = new Role("ROLE_ADMIN");

        roleRepository.saveAll(List.of(ROLE_USER, ROLE_ADMIN));

        user = new User();
        user.setUsername("Gosho");
        user.setFirstName("Gosho");
        user.setLastName("Goshev");
        user.setEmail("gosho@abv.bg");
        user.setPassword("123");
        user.setProfilePhoto("asd/asd/asd.jpg");
        user.setId("1");
        user.setAddresses(new LinkedHashSet<>());
        user.setShoppingCart(new ShoppingCart());

        when(cloudinaryService.createPhoto(file, "a", "b")).thenReturn("asd.jpg");
        Mockito.when(modelMapper.map(any(UserServiceModel.class), eq(User.class)))
                .thenAnswer(invocation -> mapper.map(invocation.getArguments()[0], User.class));

        Mockito.when(modelMapper.map(any(User.class), eq(UserServiceModel.class)))
                .thenAnswer(invocation -> mapper.map(invocation.getArguments()[0], UserServiceModel.class));

        Mockito.when(modelMapper.map(any(Role.class), eq(RoleServiceModel.class)))
                .thenAnswer(invocation -> mapper.map(invocation.getArguments()[0], RoleServiceModel.class));

        Mockito.when(modelMapper.map(any(RoleServiceModel.class), eq(Role.class)))
                .thenAnswer(invocation -> mapper.map(invocation.getArguments()[0], Role.class));
        when(userRepository.save(any())).thenReturn(any());
//        when(encoder.encode(any())).thenAnswer(invocationOnMock -> actualEncoder.encode((CharSequence) invocationOnMock.getArguments()[0]));
//        when(encoder.matches(any(), any())).thenAnswer(invocationOnMock -> actualEncoder.matches((String) invocationOnMock.getArguments()[0], (String) invocationOnMock.getArguments()[1]));
    }

    @Test
    public void getUserByUsername_whenValidUsername_shouldReturnUser() {

        when(this.userRepository.findByUsername(any()))
                .thenReturn(Optional.of(user));
        User userModel = this.userService.getUserByUsername(user.getUsername());
        Assert.assertEquals(user.getFirstName(), userModel.getFirstName());
        Assert.assertEquals(user.getLastName(), userModel.getLastName());
        Assert.assertEquals(user.getEmail(), userModel.getEmail());

    }

    @Test
    public void getUserByUsername_whenInvalidUsername_shouldThrow() {
        when(this.userRepository.findByUsername(any()))
                .thenReturn(Optional.empty());

        assertThrows(
                UsernameNotFoundException.class,
                () -> this.userService.getUserByUsername("asd"));
    }

    @Test
    void registerUser_whenValid_ShouldCreateAndReturnSameUser() throws IOException {
        UserServiceModel act = new UserServiceModel();
        user.setUsername("Gosho");
        user.setFirstName("Gosho");
        user.setLastName("Goshev");
        user.setEmail("gosho@abv.bg");
        user.setPassword("123");
        user.setProfilePhoto("asd/asd/asd.jpg");
//        user.setId("1");

        when(roleRepository.findAll()).thenReturn(List.of(new Role("ROLE_USER")));

        UserServiceModel user = userService.registerUser(act, file);

        verify(userRepository).save(any());

        assertEquals("Gosho", user.getUsername());
    }

    @Test
    void addAddressToUser() {
    }

    @Test
    void saveUser() {
    }

    @Test
    void editUser() {
    }

    @Test
    void getAllUsers() {
    }

    @Test
    void addRoleToUser() {
    }

    @Test
    void deleteUser() {
    }

}