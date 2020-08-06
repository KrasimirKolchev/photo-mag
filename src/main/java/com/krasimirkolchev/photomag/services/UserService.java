package com.krasimirkolchev.photomag.services;

import com.krasimirkolchev.photomag.models.bindingModels.AddressAddBindingModel;
import com.krasimirkolchev.photomag.models.bindingModels.UserRoleAddBindingModel;
import com.krasimirkolchev.photomag.models.entities.ShoppingCart;
import com.krasimirkolchev.photomag.models.entities.User;
import com.krasimirkolchev.photomag.models.serviceModels.AddressServiceModel;
import com.krasimirkolchev.photomag.models.serviceModels.UserServiceModel;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

public interface UserService extends UserDetailsService {
    boolean existByUsername(String username);

    boolean existByEmail(String email);

    User getUserById(String id);

    User getUserByUsername(String username);

    UserServiceModel registerUser(UserServiceModel userServiceModel, MultipartFile file) throws IOException;

    UserServiceModel addAddressToUser(AddressAddBindingModel addressAddBindingModel, Principal principal);

    UserServiceModel saveUser(User user);

    UserServiceModel editUser(UserServiceModel userServiceModel, String oldPsw, String username);

    List<UserServiceModel> getAllUsers();

    void addRoleToUser(UserRoleAddBindingModel userRoleAddBindingModel);

    void deleteUser(String username);
}
