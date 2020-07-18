package com.krasimirkolchev.photomag.services.impl;

import com.krasimirkolchev.photomag.models.entities.Role;
import com.krasimirkolchev.photomag.repositories.RoleRepository;
import com.krasimirkolchev.photomag.services.RoleService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository, ModelMapper modelMapper) {
        this.roleRepository = roleRepository;
        this.modelMapper = modelMapper;
    }

    @PostConstruct
    public void initRoles() {
        if (this.roleRepository.count() == 0) {
            Role rootAdmin = new Role("ROLE_ROOT_ADMIN");
            Role admin = new Role("ROLE_ADMIN");
            Role user = new Role("ROLE_USER");

            this.roleRepository.save(rootAdmin);
            this.roleRepository.save(admin);
            this.roleRepository.save(user);
        }
    }

    @Override
    public Role findByName(String name) {
        return this.roleRepository.findByAuthority(name)
                .orElse(null);
    }

    @Override
    public Set<Role> getAllRoles() {
        return new HashSet<>(this.roleRepository.findAll());
    }
}
