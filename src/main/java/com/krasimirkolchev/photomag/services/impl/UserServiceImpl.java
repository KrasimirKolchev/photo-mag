package com.krasimirkolchev.photomag.services.impl;

import com.krasimirkolchev.photomag.models.bindingModels.UserRoleAddBindingModel;
import com.krasimirkolchev.photomag.models.entities.Role;
import com.krasimirkolchev.photomag.models.entities.ShoppingCart;
import com.krasimirkolchev.photomag.models.entities.User;
import com.krasimirkolchev.photomag.models.serviceModels.*;
import com.krasimirkolchev.photomag.repositories.UserRepository;
import com.krasimirkolchev.photomag.services.AddressService;
import com.krasimirkolchev.photomag.services.RoleService;
import com.krasimirkolchev.photomag.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleService roleService;
    private final AddressService addressService;
    private final CloudinaryServiceImpl cloudinaryService;
    private final BCryptPasswordEncoder encoder;
    private final ModelMapper modelMapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleService roleService, AddressService addressService,
                           CloudinaryServiceImpl cloudinaryService, BCryptPasswordEncoder encoder, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.addressService = addressService;
        this.cloudinaryService = cloudinaryService;
        this.encoder = encoder;
        this.modelMapper = modelMapper;
    }

    @Override
    public User getUserByUsername(String username) {
        return this.userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found!"));
    }

    @Override
    public UserServiceModel registerUser(UserServiceModel userServiceModel, MultipartFile file) throws IOException {

        this.roleService.initRoles();

        setUserRole(userServiceModel);

        User user = this.modelMapper.map(userServiceModel, User.class);
        user.setPassword(this.encoder.encode(userServiceModel.getPassword()));
        user.setShoppingCart(new ShoppingCart());
        user.setProfilePhoto(this.cloudinaryService
                .createPhoto(file, "users", userServiceModel.getUsername()));

        return this.saveUser(user);
    }

    private void setUserRole(UserServiceModel userEntity) {
        if (this.userRepository.count() == 0) {
            userEntity.setAuthorities(this.roleService.getAllRoles());
        } else {
            RoleServiceModel role = this.roleService.findByName("ROLE_USER");
            userEntity.setAuthorities(new HashSet<>());
            userEntity.getAuthorities().add(role);
        }
    }

    @Override
    public UserServiceModel addAddressToUser(AddressServiceModel addressServiceModel, String name) {

        UserServiceModel user = this.modelMapper.map(this.getUserByUsername(name), UserServiceModel.class);
        user.getAddresses().add(addressServiceModel);

        this.userRepository.save(this.modelMapper.map(user, User.class));
        return user;
    }

    @Override
    public UserServiceModel saveUser(User user) {
        return this.modelMapper.map(this.userRepository.save(user), UserServiceModel.class);
    }

    @Override
    public UserServiceModel editUser(UserServiceModel userServiceModel, String oldPsw, String username) {
        User user = this.userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with username " + username + " not found!"));

        user.setPassword(!userServiceModel.getPassword().equals("") ? this.encoder.encode(userServiceModel.getPassword()) : user.getPassword());
        user.setFirstName(userServiceModel.getFirstName());
        user.setLastName(userServiceModel.getLastName());

        return this.saveUser(user);
    }

    //get all other isdeleted=false users except ROOT_ADMIN as only he can make role changes
    @Override
    public List<UserServiceModel> getAllUsers() {
        return this.userRepository.findAllByDeletedIsFalse()
                .stream()
                .filter(u -> {
                    for (Role r : u.getAuthorities()) {
                        if (r.getAuthority().contains("ROLE_ROOT_ADMIN")) {
                            return false;
                        }
                    }
                    return true;
                })
                .map(u -> this.modelMapper.map(u, UserServiceModel.class))
                .collect(Collectors.toList());
    }

    //to get all users emails, not included ROOT_ADMIN and ADMIN
    @Override
    public List<String> getAllUsersEmail() {
        return this.userRepository.findAllByDeletedIsFalse()
                .stream()
                .filter(u -> {
                    if (u.getAuthorities().size() == 1) {
                        for (Role r : u.getAuthorities()) {
                            if (r.getAuthority().contains("ROLE_USER")) {
                                return true;
                            }
                        }
                    }
                    return false;
                })
                .map(User::getEmail)
                .collect(Collectors.toList());
    }

    @Override
    public void addRoleToUser(UserRoleAddBindingModel userRoleAddBindingModel) {
        UserServiceModel userServiceModel = this.modelMapper
                .map(this.getUserByUsername(userRoleAddBindingModel.getUsername()), UserServiceModel.class);
        userServiceModel.getAuthorities().add(this.modelMapper
                .map(this.roleService.findByName(userRoleAddBindingModel.getRole()), RoleServiceModel.class));
        this.saveUser(this.modelMapper.map(userServiceModel, User.class));
    }

    @Override
    public void deleteUser(String username) {
        User user = this.getUserByUsername(username);
        user.setDeleted(true);
        user.setDeleteDate(LocalDateTime.now());
        this.userRepository.save(user);
    }

    //will check for users that are isDeleted=true and will delete from repo after 7-th day
    @Async
    @Scheduled(cron = "0 0 1 * * *")
    void deleteUsers() {
        List<User> users = this.userRepository.findAllByDeletedIsTrue();

        for (User u : users) {
            if (u.getDeleteDate().plusDays(7).isBefore(LocalDateTime.now())) {
                this.userRepository.deleteById(u.getId());
            }
        }
    }
}
