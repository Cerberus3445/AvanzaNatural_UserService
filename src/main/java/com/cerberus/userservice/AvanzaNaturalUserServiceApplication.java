package com.cerberus.userservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class AvanzaNaturalUserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AvanzaNaturalUserServiceApplication.class, args);
    }

}
