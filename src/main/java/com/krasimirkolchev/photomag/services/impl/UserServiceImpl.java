package com.krasimirkolchev.photomag.services.impl;

import com.krasimirkolchev.photomag.models.bindingModels.AddressAddBindingModel;
import com.krasimirkolchev.photomag.models.entities.Address;
import com.krasimirkolchev.photomag.models.entities.ShoppingCart;
import com.krasimirkolchev.photomag.models.entities.User;
import com.krasimirkolchev.photomag.models.entities.UserPrincipal;
import com.krasimirkolchev.photomag.models.serviceModels.AddressServiceModel;
import com.krasimirkolchev.photomag.models.serviceModels.UserServiceModel;
import com.krasimirkolchev.photomag.repositories.UserRepository;
import com.krasimirkolchev.photomag.services.AddressService;
import com.krasimirkolchev.photomag.services.RoleService;
import com.krasimirkolchev.photomag.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.Principal;
import java.util.Set;

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

    @PostConstruct
    private void init() {
        if (this.userRepository.count() == 0) {
            User rootAdmin = new User("rootadmin", "password", "email@email.com", "Root", "Admin");
            rootAdmin.setAuthorities(this.roleService.getAllRoles());
            rootAdmin.setPassword(encoder.encode(rootAdmin.getPassword()));
            rootAdmin.setShoppingCart(new ShoppingCart());
            rootAdmin.setProfilePhoto("https://res.cloudinary.com/dk8gbxoue/image/upload/v1596182264/temp_user_photo/user-picture_teq4ct.jpg");
            this.userRepository.saveAndFlush(rootAdmin);
        }
    }

    @Override
    public boolean existByUsername(String username) {
        return this.userRepository.existsByUsername(username);
    }

    @Override
    public boolean existByEmail(String email) {
        return this.userRepository.existsByEmail(email);
    }

    @Override
    public User getUserById(String id) {
        return this.userRepository.findById(id).orElse(null);
    }

    @Override
    public User getUserByUsername(String username) {
        return this.userRepository.findByUsername(username).orElse(null);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.userRepository.findByUsername(username).orElse(null);

        if (user == null) {
            throw new UsernameNotFoundException("User with username " + username + " not found!");
        }

        return new UserPrincipal(user);
    }

    @Override
    public UserServiceModel registerUser(UserServiceModel userServiceModel, MultipartFile file) throws IOException {

        if (this.existByUsername(userServiceModel.getUsername())) {
            throw new EntityExistsException(String.format("Username: %s already exist!", userServiceModel.getUsername()));
        }

        if (this.existByEmail(userServiceModel.getEmail())) {
            throw new EntityExistsException(String.format("User with email: %s already exist!", userServiceModel.getEmail()));
        }


        User user = this.modelMapper.map(userServiceModel, User.class);
        user.setAuthorities(Set.of(this.roleService.findByName("ROLE_USER")));

        if (file == null || file.isEmpty() || file.getOriginalFilename().length() == 0) {
            throw new FileNotFoundException("File is empty!");
        }

        user.setProfilePhoto(this.cloudinaryService.createPhoto(file, "users", user.getUsername()));
        user.setPassword(this.encoder.encode(userServiceModel.getPassword()));
        user.setShoppingCart(new ShoppingCart());
        return this.modelMapper.map(this.userRepository.save(user), UserServiceModel.class);
    }

    @Override
    public User addPhotoToUserGallery(Principal principal, MultipartFile file) throws IOException {
        User user = this.getUserByUsername(principal.getName());

        if (file == null || file.isEmpty() || file.getOriginalFilename().length() == 0) {
            throw new FileNotFoundException("File is empty!");
        }
        user.getGallery().add(this.cloudinaryService.createPhoto(file, "users", principal.getName()));
        return this.userRepository.save(user);
    }

    @Override
    public UserServiceModel addAddressToUser(AddressAddBindingModel addressAddBindingModel, Principal principal) {

        UserServiceModel user = this.modelMapper.map(this.getUserByUsername(principal.getName()), UserServiceModel.class);
        user.getAddresses().add(this.addressService.createAddress(this.modelMapper
                .map(addressAddBindingModel, AddressServiceModel.class)));

        this.userRepository.save(this.modelMapper.map(user, User.class));
        return user;
    }

    @Override
    public User saveUser(User user) {
        return this.userRepository.save(user);
    }

    @Override
    public UserServiceModel editUser(UserServiceModel userServiceModel, String oldPsw, String username) {
        User user = this.userRepository.findByUsername(username).orElseThrow(() -> new EntityNotFoundException(""));

        if (!this.encoder.matches(oldPsw, user.getPassword())) {
            throw new IllegalArgumentException("Incorrect old password!");
        }

        user.setPassword(userServiceModel.getPassword() != null ? this.encoder.encode(userServiceModel.getPassword()) : user.getPassword());
        user.setFirstName(userServiceModel.getFirstName());
        user.setLastName(userServiceModel.getLastName());

        return this.modelMapper.map(this.userRepository.save(user), UserServiceModel.class);
    }

}
