package com.krasimirkolchev.photomag.integration;

import com.krasimirkolchev.photomag.models.bindingModels.UserRegBindingModel;
import com.krasimirkolchev.photomag.models.bindingModels.UserRoleAddBindingModel;
import com.krasimirkolchev.photomag.models.entities.Role;
import com.krasimirkolchev.photomag.models.entities.User;
import com.krasimirkolchev.photomag.repositories.RoleRepository;
import com.krasimirkolchev.photomag.repositories.UserRepository;
import com.krasimirkolchev.photomag.web.controllers.UserController;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithAnonymousUser;
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
import java.security.Principal;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext wac;
    @Autowired
    private UserRepository repository;
    @Autowired
    private UserController controller;
    @Autowired
    private BCryptPasswordEncoder encoder;
    @Autowired
    private RoleRepository roleRepository;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
        repository.deleteAll();
    }

    @Test
    @WithAnonymousUser
    public void registerUserReturnsCorrectView() throws Exception {
        this.mockMvc.perform(get("/users/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"));
    }

    @Test
    @WithAnonymousUser
    public void registerUserConfRegisterTheUserWhenValid() throws Exception {
        File file2 = new File("src/main/resources/static/img/user-picture.jpg");
        MockMultipartFile file = new MockMultipartFile("user-picture", "user-picture.jpg",
                MediaType.IMAGE_JPEG_VALUE, FileCopyUtils.copyToByteArray(file2));

        RedirectAttributes attributes = null;

        UserRegBindingModel model = new UserRegBindingModel();
        model.setUsername("admina");
        model.setEmail("aaa@asd.as");
        model.setFirstName("admin");
        model.setLastName("adminov");
        model.setPassword("password");
        model.setConfirmPassword("password");
        model.setFile(file);

        BindingResult result = new BeanPropertyBindingResult(model, "userRegBindingModel");
        controller.userRegisterConf(model, result, null);

        Assert.assertEquals(1L, repository.count());
    }

    @Test
    @WithAnonymousUser
    public void registerUserConfWhenInvalidUserRedirects() throws Exception {
        this.mockMvc.perform(post("/users/register")
                .param("username", "username")
                .param("password", "password")
                .param("confirmPassword", "password")
                .param("firstName", "admin")
                .param("lastName", "adminov")
                .param("email", "asd")
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/users/register"));
    }

    @Test
    @WithAnonymousUser
    public void loginUserReturnsCorrectView() throws Exception {
        this.mockMvc.perform(get("/users/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    @WithMockUser(username = "admina", roles = {"USER", "ADMIN"})
    public void userProfileReturnsCorrectView() throws Exception {
        Principal principal = Mockito.mock(Principal.class);
        Mockito.when(principal.getName()).thenReturn("admina");

        repository.deleteAll();
        User user = new User();
        user.setUsername("admina");
        user.setEmail("asd@asd.as");
        user.setFirstName("admina");
        user.setLastName("adminova");
        user.setPassword("password");
        user.setProfilePhoto("file");
        repository.save(user);

        mockMvc.perform(get("/users/profile"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("profile"));
    }

    @Test
    @WithMockUser(username = "admina", roles = {"USER", "ADMIN"})
    public void editUserReturnsCorrectView() throws Exception {
        Principal principal = Mockito.mock(Principal.class);
        Mockito.when(principal.getName()).thenReturn("admina");

        User user = new User();
        user.setUsername("admina");
        user.setEmail("asd@asd.as");
        user.setFirstName("admina");
        user.setLastName("adminova");
        user.setPassword("password");
        user.setProfilePhoto("file");
        repository.save(user);

        this.mockMvc.perform(get("/users/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("edit-user"));
    }

    @Test
    @WithMockUser(username = "admina", roles = {"USER", "ADMIN"})
    public void editUserEditsUser() throws Exception {
        Principal principal = Mockito.mock(Principal.class);
        Mockito.when(principal.getName()).thenReturn("admina");

        repository.deleteAll();

        User user = new User();
        user.setUsername("admina");
        user.setEmail("asd@asd.as");
        user.setFirstName("admina");
        user.setLastName("adminova");
        user.setPassword(encoder.encode("password"));
        user.setProfilePhoto("file");
        User u = repository.save(user);

        this.mockMvc.perform(post("/users/edit")
                .param("id", u.getId())
                .param("firstName", "new name")
                .param("lastName", "new lastName")
                .param("oldPassword", "password")
                .param("password", "")
                .param("ConfirmPassword", "")

        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/users/profile"));
    }

    @Test
    @WithMockUser(username = "admina", roles = {"USER", "ADMIN"})
    public void deleteUserSetDeletedTrue() throws Exception {
        Principal principal = Mockito.mock(Principal.class);
        Mockito.when(principal.getName()).thenReturn("admina");

        repository.deleteAll();

        User user = new User();
        user.setUsername("admina");
        user.setEmail("asd@asd.as");
        user.setFirstName("admina");
        user.setLastName("adminova");
        user.setPassword(encoder.encode("password"));
        user.setProfilePhoto("file");
        user.setDeleted(false);
        User u = repository.save(user);

        this.mockMvc.perform(get("/users/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/logout"));

        User deleted = repository.findAllByDeletedIsTrue().get(0);
        Assert.assertTrue(deleted.isDeleted());
    }

    @Test
    @WithMockUser(username = "admina", roles = {"ROOT_ADMIN", "ADMIN"})
    public void addRoleToUserReturnsCorrectView() throws Exception {
        this.mockMvc.perform(get("/users/roles/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("user-add-role"));
    }

    @Test
    @WithMockUser(username = "admina", roles = {"ROOT_ADMIN", "ADMIN"})
    public void addRoleToUserConfAddsRole() throws Exception {
        Principal principal = Mockito.mock(Principal.class);
        Mockito.when(principal.getName()).thenReturn("admina");

        repository.deleteAll();

        User u = new User();
        u.setUsername("admin");
        u.setEmail("aaaa@asd.as");
        u.setFirstName("admin");
        u.setLastName("adminov");
        u.setPassword(encoder.encode("password"));
        u.setProfilePhoto("file");
        u.setAuthorities(Set.of(roleRepository.saveAndFlush(new Role("ROLE_USER"))));
        u = repository.saveAndFlush(u);

        Assert.assertEquals(1, u.getAuthorities().size());

        roleRepository.saveAndFlush(new Role("ROLE_ADMIN"));

        this.mockMvc.perform(post("/users/roles/add")
                .param("username", "admin")
                .param("role", "ROLE_ADMIN")
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/home"));

        User test = repository.findByUsername("admin").orElse(null);
        Assert.assertEquals(2, test.getAuthorities().size());
    }

}
