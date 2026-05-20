package com.chronoaegis.personagens_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class PersonagensServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(PersonagensServiceApplication.class, args);
    }
}