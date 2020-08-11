package com.krasimirkolchev.photomag.services.impl;

import com.krasimirkolchev.photomag.models.entities.Role;
import com.krasimirkolchev.photomag.models.serviceModels.RoleServiceModel;
import com.krasimirkolchev.photomag.repositories.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityNotFoundException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
class RoleServiceImplTest {
    private RoleServiceImpl roleService;
    private RoleRepository roleRepository;
    private ModelMapper modelMapper;
    private Role roleEnt;

    @BeforeEach
    void setup() {
        roleRepository = Mockito.mock(RoleRepository.class);
        modelMapper = Mockito.mock(ModelMapper.class);
        roleService = new RoleServiceImpl(roleRepository, modelMapper);

        Mockito.when(roleRepository.findByAuthority(anyString())).thenReturn(Optional.of(new Role() {{
            setAuthority("USER");
        }}));

        ModelMapper mapper = new ModelMapper();

        Mockito.when(modelMapper.map(any(RoleServiceModel.class), eq(Role.class)))
                .thenAnswer(invocation -> mapper.map(invocation.getArguments()[0], Role.class));

        Mockito.when(modelMapper.map(any(Role.class), eq(RoleServiceModel.class)))
                .thenAnswer(invocation -> mapper.map(invocation.getArguments()[0], RoleServiceModel.class));

    }

    @Test
    void initRolesInDBWhenNoRolesShouldSeed() {
        Mockito.when(roleRepository.count()).thenReturn(0L);
        this.roleService.initRoles();

        ArgumentCaptor<Role> argument = ArgumentCaptor.forClass(Role.class);
        Mockito.verify(roleRepository, times(3)).save(argument.capture());

        List<Role> roles = argument.getAllValues();
        assertEquals(3, roles.size());
        assertEquals("ROLE_ROOT_ADMIN", roles.get(0).getAuthority());
        assertEquals("ROLE_ADMIN", roles.get(1).getAuthority());
        assertEquals("ROLE_USER", roles.get(2).getAuthority());
    }

    @Test
    void initRolesInDbWhenAreRolesNotSeed() {

    }

    @Test
    void findByNameReturnsRoleIfRoleExist() {
        RoleServiceModel exp = roleService.findByName("USER");

        assertEquals(exp.getAuthority(), "USER");
    }

    @Test
    void findByNameThrowsIfNotExist() {
        when(this.roleRepository.findByAuthority(anyString())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> this.roleService.findByName("invalid"));
    }

    @Test
    void getAllRoles() {
        Role role1 = new Role("ROLE_USER");
        Role role2 = new Role("ROLE_ADMIN");
        List<Role> roles = new ArrayList<>();
        roles.add(role1);
        roles.add(role2);

        Mockito.when(roleRepository.findAll()).thenReturn(roles);
        Set<RoleServiceModel> result = roleService.getAllRoles();

        assertEquals(roles.size(), result.size());
    }
}