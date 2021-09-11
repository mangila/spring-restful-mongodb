package com.github.mangila.springbootrestfulservice.web.resource.v1.testcontainers;

import com.github.mangila.springbootrestfulservice.web.repository.v1.OrderRepository;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

@Tag("testcontainers")
@DataMongoTest(excludeAutoConfiguration = EmbeddedMongoAutoConfiguration.class)
public class OrderResourceDataMongoTestContainersTest extends SeededMongoDBTestContainer {

    private final String orderId = "2cdf9886-f565-4d5f-8bbc-2561806b2052";

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private OrderRepository repository;

    @Test
    void findAll() {

    }

}
