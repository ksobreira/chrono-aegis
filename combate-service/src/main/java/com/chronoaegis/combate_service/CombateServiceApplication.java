package com.chronoaegis.combate_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class CombateServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(CombateServiceApplication.class, args);
    }
}
