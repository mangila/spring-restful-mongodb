package com.github.mangila.springbootrestfulservice.web.resource;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.mangila.springbootrestfulservice.bootstrap.DatabaseSeeder;
import com.github.mangila.springbootrestfulservice.persistence.domain.CustomerDocument;
import com.github.mangila.springbootrestfulservice.persistence.domain.OrderDocument;
import com.github.mangila.springbootrestfulservice.persistence.repository.CustomerRepository;
import com.github.mangila.springbootrestfulservice.persistence.repository.OrderRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.List;

@Component
public abstract class SeededEmbeddedMongo {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private OrderRepository orderRepository;

    @BeforeEach
    void beforeEach() throws IOException {
        var mapper = new ObjectMapper()
                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                .registerModule(new JavaTimeModule());
        URL url = DatabaseSeeder.class.getResource("/customer-dev-schema.json");
        var customers = mapper
                .readValue(url, new TypeReference<List<CustomerDocument>>() {
                });
        this.customerRepository.insert(customers);
        url = DatabaseSeeder.class.getResource("/order-dev-schema.json");
        var orders = mapper
                .readValue(url, new TypeReference<List<OrderDocument>>() {
                });
        this.orderRepository.insert(orders);
    }

    @AfterEach
    void afterEach() {
        this.customerRepository.deleteAll();
        this.orderRepository.deleteAll();
    }

}
