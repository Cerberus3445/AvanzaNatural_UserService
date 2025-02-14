package com.cerberus.userservice;

import org.springframework.boot.SpringApplication;

public class TestAvanzaNaturalUserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.from(AvanzaNaturalUserServiceApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
