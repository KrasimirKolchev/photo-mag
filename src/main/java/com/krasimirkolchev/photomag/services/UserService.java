package com.krasimirkolchev.photomag.services;

import com.krasimirkolchev.photomag.models.entities.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;

public interface UserService extends UserDetailsService {
    boolean existByUsername(String username);

    boolean existByEmail(String email);

    User getUserById(String id);

    User getUserByUsername(String username);

    User registerUser(User user, MultipartFile file) throws IOException;

    User addPhotoToUserGallery(Principal principal, MultipartFile file) throws IOException;
}
