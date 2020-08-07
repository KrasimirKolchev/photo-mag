package com.krasimirkolchev.photomag.config;

import com.cloudinary.*;
import com.cloudinary.utils.ObjectUtils;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.validation.Validation;
import java.util.Objects;

@Configuration
public class ApplicationConfiguration {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public Validation validation() {
        return new Validation();
    }

    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dk8gbxoue",
                "api_key", "311932535255713",
                "api_secret", "67preLKTuPtUFxACTWRUQAOLYa8"));
    }
}
