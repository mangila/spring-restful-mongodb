package com.github.mangila.springbootrestfulservice.web.resource.v1.testcontainers;

import com.github.mangila.springbootrestfulservice.web.repository.v1.CustomerRepository;
import com.github.mangila.springbootrestfulservice.web.service.v1.CustomerService;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;

@Tag("testcontainers")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@EnableAutoConfiguration(exclude = EmbeddedMongoAutoConfiguration.class)
@Testcontainers
public class CustomerResourceSpringBootTestContainersTest extends AbstractMongoDBTestContainer {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private CustomerService service;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private CustomerRepository repository;

    @Test
    void findAll() {

    }

    @Test
    void findById() {

    }

    @Test
    void deleteById() {

    }

    @Test
    void insert() {

    }

    @Test
    void update() {

    }

}
