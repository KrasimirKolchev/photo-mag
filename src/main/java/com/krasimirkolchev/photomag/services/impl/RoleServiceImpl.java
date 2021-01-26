package com.krasimirkolchev.photomag.services.impl;

import com.krasimirkolchev.photomag.models.entities.Role;
import com.krasimirkolchev.photomag.models.serviceModels.RoleServiceModel;
import com.krasimirkolchev.photomag.repositories.RoleRepository;
import com.krasimirkolchev.photomag.services.RoleService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.persistence.EntityNotFoundException;
import java.util.List;
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

    @Override
    public void initRoles() {
        if (this.roleRepository.count() == 0) {
            this.roleRepository.save(new Role("ROLE_ROOT_ADMIN"));
            this.roleRepository.save(new Role("ROLE_ADMIN"));
            this.roleRepository.save(new Role("ROLE_USER"));
        }
    }

    @Override
    public RoleServiceModel findByName(String name) {
        Role role = this.roleRepository.findByAuthority(name)
                .orElseThrow(() -> new EntityNotFoundException("Role doesn't exist!"));
        return this.modelMapper.map(role, RoleServiceModel.class);
    }

    @Override
    public Set<RoleServiceModel> getAllRoles() {
        return this.roleRepository.findAll()
                .stream().map(r -> this.modelMapper.map(r, RoleServiceModel.class))
                .collect(Collectors.toSet());
    }
}
