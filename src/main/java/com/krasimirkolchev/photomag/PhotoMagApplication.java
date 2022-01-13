package com.krasimirkolchev.photomag;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PhotoMagApplication {

    public static void main(String[] args) {
        SpringApplication.run(PhotoMagApplication.class, args);
    }

    // card for testing: 4242 4242 4242 4242, validity: future date, CVC: any 3 digits

}
