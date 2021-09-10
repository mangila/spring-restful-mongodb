package com.github.mangila.springbootrestfulservice.web.resource.v1.datamongo;

import com.github.mangila.springbootrestfulservice.web.repository.v1.OrderRepository;
import com.github.mangila.springbootrestfulservice.web.resource.v1.EmbeddedMongoDatabaseSeed;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("datamongo")
@DataMongoTest
public class OrderResourceDataMongoTest extends EmbeddedMongoDatabaseSeed {

    @Autowired
    private OrderRepository repository;

    private final String testId = "2cdf9886-f565-4d5f-8bbc-2561806b2052";

    @Test
    void findAll() {
        assertEquals(11, this.repository.findAll().size());
        assertEquals(11, this.repository.count());
    }

    @Test
    void findById() {
        assertTrue(this.repository.existsById(this.testId));
        assertTrue(this.repository.findById(this.testId).isPresent());
    }

    @Test
    void deleteById() {
        this.repository.deleteById(this.testId);
        assertEquals(10, this.repository.count());
        assertEquals(10, this.repository.findAll().size());
    }

}
