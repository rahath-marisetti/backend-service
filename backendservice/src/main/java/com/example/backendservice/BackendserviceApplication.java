package com.example.backendservice;

import org.springframework.boot.SpringApplication;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.example.backendservice")


public class BackendserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendserviceApplication.class, args);
		System.out.println("Push Test");
	}

}
