package com.github.mangila.springbootrestfulservice.web.resource.v1.datamongo;

import com.github.mangila.springbootrestfulservice.domain.OrderDocument;
import com.github.mangila.springbootrestfulservice.web.repository.v1.OrderRepository;
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
public class OrderResourceDataMongoTest {

    @Autowired
    private OrderRepository repository;

    @AfterEach
    void afterEach() {
        this.repository.deleteAll();
    }

    @Test
    void findAll() {
        this.repository.saveAll(Lists.newArrayList(
                new OrderDocument(),
                new OrderDocument(),
                new OrderDocument()
        ));
        assertEquals(3, this.repository.findAll().size());
        assertEquals(3, this.repository.count());
    }

    @Test
    void findById() {
        String id = this.repository.insert(new OrderDocument()).getId();
        assertTrue(this.repository.existsById(id));
        assertTrue(this.repository.findById(id).isPresent());
    }

    @Test
    void updateOrder() {
        OrderDocument orderDocument = new OrderDocument();
        orderDocument.setAmount(2000);
        String id = this.repository.insert(orderDocument).getId();
        orderDocument = this.repository.findById(id).orElseThrow();
        assertEquals(2000, orderDocument.getAmount());
        orderDocument.setAmount(3000);
        this.repository.save(orderDocument);
        assertEquals(3000, this.repository.findById(id).get().getAmount());
    }

    @Test
    void deleteById() {
        String id = this.repository.insert(new OrderDocument()).getId();
        assertEquals(1, this.repository.count());
        this.repository.deleteById(id);
        assertEquals(0, this.repository.count());
    }

}
