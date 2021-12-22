package com.github.mangila.springbootrestfulservice.web.resource.testcontainers;

import com.github.mangila.springbootrestfulservice.persistence.domain.CustomerDocument;
import com.github.mangila.springbootrestfulservice.persistence.repository.CustomerRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

@Tag("testcontainers")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DataMongoTest(excludeAutoConfiguration = EmbeddedMongoAutoConfiguration.class)
public class CustomerResourceDataMongoTestContainersTest extends SeededMongoDBTestContainer {

    private final String customerId = "c8127464-3559-45ca-a70e-51f9c3a6d1c0";

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private CustomerRepository repository;

    @Order(100)
    @Test
    void findAll() {
        Assertions.assertEquals(5, this.repository.findAll().size());
        Assertions.assertEquals(5, this.repository.count());
    }

    @Order(200)
    @Test
    void findById() {
        CustomerDocument c = this.repository.findById(this.customerId).get();
        Assertions.assertNotNull(c);
        Assertions.assertEquals("Aragorn", c.getName());
    }

    @Order(300)
    @Test
    void update() {
        CustomerDocument c = this.repository.findById(this.customerId).get();
        c.setName("Smegol");
        this.repository.save(c);

        c = this.repository.findById(this.customerId).get();
        Assertions.assertNotNull(c);
        Assertions.assertEquals("Smegol", c.getName());
    }

    @Order(400)
    @Test
    void insert() {
        CustomerDocument c = new CustomerDocument();
        c.setName("Bilbo");
        this.repository.insert(c);
        Assertions.assertEquals(6, this.repository.findAll().size());
        Assertions.assertEquals(6, this.repository.count());
    }

    @Order(500)
    @Test
    void deleteById() {
        this.repository.deleteById(this.customerId);
        Assertions.assertEquals(5, this.repository.findAll().size());
        Assertions.assertEquals(5, this.repository.count());
    }


}
