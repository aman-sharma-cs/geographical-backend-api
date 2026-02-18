package com.example.geographical;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class GeographicalApplication {

	public static void main(String[] args) {
		SpringApplication.run(GeographicalApplication.class, args);
	}

}

