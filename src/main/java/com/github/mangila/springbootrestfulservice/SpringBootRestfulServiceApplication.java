package com.github.mangila.springbootrestfulservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class SpringBootRestfulServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootRestfulServiceApplication.class, args);
	}

}
