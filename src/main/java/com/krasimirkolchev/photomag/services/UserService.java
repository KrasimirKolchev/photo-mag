package com.krasimirkolchev.photomag.services;

import com.krasimirkolchev.photomag.models.bindingModels.UserRoleAddBindingModel;
import com.krasimirkolchev.photomag.models.entities.User;
import com.krasimirkolchev.photomag.models.serviceModels.AddressServiceModel;
import com.krasimirkolchev.photomag.models.serviceModels.UserServiceModel;
import com.krasimirkolchev.photomag.models.serviceModels.VoucherServiceModel;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

public interface UserService {

    User getUserByUsername(String username);

    UserServiceModel registerUser(UserServiceModel userServiceModel, MultipartFile file) throws IOException;

    UserServiceModel addAddressToUser(AddressServiceModel addressServiceModel, String name);

    UserServiceModel saveUser(User user);

    UserServiceModel editUser(UserServiceModel userServiceModel, String oldPsw, String username);

    List<UserServiceModel> getAllUsers();

    List<String> getAllUsersEmail();

    void addRoleToUser(UserRoleAddBindingModel userRoleAddBindingModel);

    void deleteUser(String username);
}
