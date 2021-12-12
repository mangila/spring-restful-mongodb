package com.github.mangila.springbootrestfulservice.web.resource.v1.testcontainers;

import com.github.mangila.springbootrestfulservice.web.dto.v1.CustomerDto;
import com.github.mangila.springbootrestfulservice.service.CustomerService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

@Tag("testcontainers")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@EnableAutoConfiguration(exclude = EmbeddedMongoAutoConfiguration.class)
public class CustomerResourceSpringBootTestContainersTest extends SeededMongoDBTestContainer {

    private final String customerId = "c8127464-3559-45ca-a70e-51f9c3a6d1c0";

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private CustomerService service;

    @Order(100)
    @Test
    void findAll() {
        Assertions.assertEquals(5, this.service.findAll().size());
        Assertions.assertFalse(this.service.findAll().isEmpty());
    }

    @Order(200)
    @Test
    void findById() {
        CustomerDto c = this.service.findById(this.customerId);
        Assertions.assertNotNull(c);
        Assertions.assertEquals("Aragorn", c.getName());
    }

    @Order(300)
    @Test
    void update() {
        CustomerDto c = new CustomerDto();
        c.setName("Smegol");
        this.service.update(this.customerId, c);

        c = this.service.findById(this.customerId);
        Assertions.assertEquals("Smegol", c.getName());
    }

    @Order(400)
    @Test
    void insert() {
        CustomerDto c = new CustomerDto();
        c.setName("Bilbo");
        this.service.insert(c);
        Assertions.assertEquals(6, this.service.findAll().size());
    }

    @Order(500)
    @Test
    void deleteById() {
        this.service.deleteById(this.customerId);
        Assertions.assertEquals(5, this.service.findAll().size());
    }

}
