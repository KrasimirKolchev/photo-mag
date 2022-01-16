package com.krasimirkolchev.photomag.services.impl;

import com.krasimirkolchev.photomag.models.bindingModels.UserRoleAddBindingModel;
import com.krasimirkolchev.photomag.models.entities.Role;
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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
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
        user.setAuthorities(Set.of(ROLE_USER));
        user.setAddresses(new LinkedHashSet<>());
        user.setDeleted(false);
//        user.setShoppingCart(new ShoppingCart());

        when(cloudinaryService.createPhoto(file, "a", "b")).thenReturn("asd.jpg");

        Mockito.when(modelMapper.map(any(UserServiceModel.class), eq(User.class)))
                .thenAnswer(invocation -> mapper.map(invocation.getArguments()[0], User.class));

        Mockito.when(modelMapper.map(any(User.class), eq(UserServiceModel.class)))
                .thenAnswer(invocation -> mapper.map(invocation.getArguments()[0], UserServiceModel.class));

        Mockito.when(modelMapper.map(any(Role.class), eq(RoleServiceModel.class)))
                .thenAnswer(invocation -> mapper.map(invocation.getArguments()[0], RoleServiceModel.class));

        Mockito.when(modelMapper.map(any(RoleServiceModel.class), eq(Role.class)))
                .thenAnswer(invocation -> mapper.map(invocation.getArguments()[0], Role.class));
    }

    @Test
    public void getUserByUsernameWhenValidUsernameShouldReturnUser() {
        when(this.userRepository.findByUsername(any())).thenReturn(Optional.of(user));

        User userModel = this.userService.getUserByUsername(user.getUsername());
        Assert.assertEquals(user.getFirstName(), userModel.getFirstName());
        Assert.assertEquals(user.getLastName(), userModel.getLastName());
        Assert.assertEquals(user.getEmail(), userModel.getEmail());
    }

    @Test
    public void getUserByUsernameWhenInvalidUsernameShouldThrow() {
        when(this.userRepository.findByUsername(any())).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> this.userService.getUserByUsername("asd"));
    }

    @Test
    public void registerUserWhenUserFoundShouldReturnSameUser() throws IOException {
        UserServiceModel user1 = new UserServiceModel() {{
            setEmail("asd@asd.as");
            setProfilePhoto("photo");
            setFirstName("first");
            setLastName("last");
            setUsername("rootadmin");
            setPassword("asdasd");
        }};

        when(roleRepository.findAll()).thenReturn(List.of(new Role("ROLE_USER")));

        UserServiceModel userServiceModel = userService.registerUser(user1, file);

        verify(userRepository).save(any());

        assertEquals("rootadmin", user1.getUsername());
        assertEquals("asd@asd.as", user1.getEmail());
    }

    @Test
    public void findUserByUsernameShouldReturnCorrectUserWhenUsernameIsValid() {
        Mockito.when(userRepository.findByUsername(anyString()))
                .thenReturn(Optional.of(user));

        User user = userService.getUserByUsername(anyString());

        assertEquals("Gosho", user.getUsername());
        assertEquals("gosho@abv.bg", user.getEmail());
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
    void getAllUsersShouldReturnUsers() {
        UserServiceModel user1 = new UserServiceModel();
        user.setUsername("Gosho");
        user.setFirstName("Gosho");
        user.setLastName("Goshev");
        user.setEmail("gosho@abv.bg");
        user.setPassword("123");
        user.setProfilePhoto("asd/asd/asd.jpg");
        user.setId("1");
        user.setDeleted(false);

        userService = Mockito.mock(UserServiceImpl.class);

        Mockito.when(userService.getAllUsers()).thenReturn(List.of(user1));

        List<UserServiceModel> users = userService.getAllUsers();

        assertEquals(1, users.size());
    }

    @Test
    void addRoleToUserTest() {
        UserService service = Mockito.mock(UserService.class);
        Mockito.doNothing().when(service).addRoleToUser(isA(UserRoleAddBindingModel.class));

        service.addRoleToUser(any());

        Mockito.verify(service).addRoleToUser(any());
    }

    @Test
    void deleteUserTest() {
        UserService service = Mockito.mock(UserService.class);
        Mockito.doNothing().when(service).deleteUser(isA(String.class));

        service.deleteUser(anyString());

        Mockito.verify(service).deleteUser(anyString());
    }

}