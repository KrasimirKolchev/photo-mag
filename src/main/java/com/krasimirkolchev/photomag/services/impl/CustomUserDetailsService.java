package com.krasimirkolchev.photomag.services.impl;

import com.krasimirkolchev.photomag.models.UserPrincipal;
import com.krasimirkolchev.photomag.models.entities.User;
import com.krasimirkolchev.photomag.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.userRepository.findByUsername(username).orElse(null);

        if (user == null) {
            throw new UsernameNotFoundException("User with username " + username + " not found!");
        }

        return new UserPrincipal(user);
    }
}
