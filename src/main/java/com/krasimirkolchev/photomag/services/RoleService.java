package com.krasimirkolchev.photomag.services;

import com.krasimirkolchev.photomag.models.entities.Role;

import java.util.Set;

public interface RoleService {

    Role findByName(String name);

    Set<Role> getAllRoles();
}
