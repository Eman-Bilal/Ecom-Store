package com.example.EcomStore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class EcomStoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(EcomStoreApplication.class, args);
		System.out.println("Hello to the E-com App");
	}

}
