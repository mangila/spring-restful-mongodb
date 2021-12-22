package com.github.mangila.springbootrestfulservice.web.resource.testcontainers;

import com.github.mangila.springbootrestfulservice.persistence.domain.Address;
import com.github.mangila.springbootrestfulservice.persistence.domain.OrderDocument;
import com.github.mangila.springbootrestfulservice.persistence.repository.OrderRepository;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

@Tag("testcontainers")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DataMongoTest(excludeAutoConfiguration = EmbeddedMongoAutoConfiguration.class)
public class OrderResourceDataMongoTestContainersTest extends SeededMongoDBTestContainer {

    private final String orderId = "2cdf9886-f565-4d5f-8bbc-2561806b2052";

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private OrderRepository repository;

    @Order(100)
    @Test
    void findAll() {
        Assertions.assertEquals(11, this.repository.findAll().size());
        Assertions.assertEquals(11, this.repository.count());
    }

    @Order(200)
    @Test
    void findById() {
        OrderDocument o = this.repository.findById(this.orderId).get();
        Assertions.assertNotNull(o);
        Assertions.assertEquals("Minas Tirith road 101", o.getAddress().getStreet());
        Assertions.assertEquals("Gondor", o.getAddress().getCity());
        Assertions.assertEquals(750, o.getAmount());
    }

    @Order(300)
    @Test
    void insert() {
        OrderDocument o = new OrderDocument();
        o.setProducts(Lists.newArrayList("Sword", "Shield"));
        Address a = new Address();
        a.setStreet("Minas Tirith road 77");
        a.setCity("Gondor");
        o.setAddress(a);
        o.setAmount(2000);
        this.repository.insert(o);

        Assertions.assertEquals(12, this.repository.findAll().size());
        Assertions.assertEquals(12, this.repository.count());
    }

    @Order(400)
    @Test
    void deleteById() {
        this.repository.deleteById(this.orderId);
        Assertions.assertEquals(11, this.repository.findAll().size());
        Assertions.assertEquals(11, this.repository.count());
    }

}
