package com.dolap.dolap;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.dolap.dolap.service.StorageService;

@SpringBootApplication
public class DolapApplication {

	public static void main(String[] args) {
		SpringApplication.run(DolapApplication.class, args);
	}

}
