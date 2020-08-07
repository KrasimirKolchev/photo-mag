package com.krasimirkolchev.photomag.services;

import com.krasimirkolchev.photomag.models.serviceModels.RoleServiceModel;

import java.util.Set;

public interface RoleService {

    RoleServiceModel findByName(String name);

    Set<RoleServiceModel> getAllRoles();

    void initRoles();
}
