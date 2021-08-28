package com.github.mangila.springbootrestfulservice.web.resource.v1.datamongo;

import com.github.mangila.springbootrestfulservice.domain.CustomerDocument;
import com.github.mangila.springbootrestfulservice.web.repository.v1.CustomerRepository;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("datamongo")
@DataMongoTest
public class CustomerResourceDataMongoTest {

    @Autowired
    private CustomerRepository repository;

    @AfterEach
    void afterEach() {
        this.repository.deleteAll();
    }

    @Test
    void findAll() {
        this.repository.saveAll(Lists.newArrayList(
                new CustomerDocument(),
                new CustomerDocument(),
                new CustomerDocument()
        ));
        assertEquals(3, this.repository.findAll().size());
        assertEquals(3, this.repository.count());
    }

    @Test
    void findById() {
        String id = this.repository.insert(new CustomerDocument()).getId();
        assertTrue(this.repository.existsById(id));
        assertTrue(this.repository.findById(id).isPresent());
    }

    @Test
    void updateCustomer() {
        CustomerDocument customerDocument = new CustomerDocument();
        customerDocument.setName("Balder");
        String id = this.repository.insert(customerDocument).getId();
        customerDocument = this.repository.findById(id).orElseThrow();
        assertEquals("Balder", customerDocument.getName());
        customerDocument.setName("Hel");
        this.repository.save(customerDocument);
        assertEquals("Hel", this.repository.findById(id).get().getName());
    }

    @Test
    void deleteById() {
        String id = this.repository.insert(new CustomerDocument()).getId();
        assertEquals(1, this.repository.count());
        this.repository.deleteById(id);
        assertEquals(0, this.repository.count());
    }

}
