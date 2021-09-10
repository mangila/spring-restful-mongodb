package com.github.mangila.springbootrestfulservice.web.resource.v1.datamongo;

import com.github.mangila.springbootrestfulservice.domain.CustomerDocument;
import com.github.mangila.springbootrestfulservice.web.repository.v1.CustomerRepository;
import com.github.mangila.springbootrestfulservice.web.resource.v1.EmbeddedMongoDatabaseSeed;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("datamongo")
@DataMongoTest
public class CustomerResourceDataMongoTest extends EmbeddedMongoDatabaseSeed {

    @Autowired
    private CustomerRepository repository;

    private final String customerId = "c8127464-3559-45ca-a70e-51f9c3a6d1c0";

    @Test
    void findAll() {
        assertEquals(5, this.repository.findAll().size());
        assertEquals(5, this.repository.count());
    }

    @Test
    void findById() {
        assertTrue(this.repository.existsById(this.customerId));
        assertTrue(this.repository.findById(this.customerId).isPresent());
    }

    @Test
    void updateCustomer() {
        CustomerDocument c = this.repository.findById(this.customerId).get();
        c.setName("Bilbo");
        this.repository.save(c);
        c = this.repository.findById(this.customerId).get();
        Assertions.assertNotEquals("Aragorn", c.getName());
        Assertions.assertEquals("Bilbo", c.getName());
    }

    @Test
    void deleteById() {
        this.repository.deleteById(this.customerId);
        assertEquals(4, this.repository.count());
    }

}
