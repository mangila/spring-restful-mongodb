package com.github.mangila.springbootrestfulservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories(basePackages = "com.github.mangila.springbootrestfulservice.repository")
public class SpringBootRestfulServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootRestfulServiceApplication.class, args);
	}

}
