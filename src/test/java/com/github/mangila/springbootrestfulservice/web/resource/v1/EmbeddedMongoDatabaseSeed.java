package com.github.mangila.springbootrestfulservice.web.resource.v1;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.mangila.springbootrestfulservice.DatabaseSeeder;
import com.github.mangila.springbootrestfulservice.domain.CustomerDocument;
import com.github.mangila.springbootrestfulservice.domain.OrderDocument;
import com.github.mangila.springbootrestfulservice.web.repository.v1.CustomerRepository;
import com.github.mangila.springbootrestfulservice.web.repository.v1.OrderRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class EmbeddedMongoDatabaseSeed {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private OrderRepository orderRepository;

    @BeforeEach
    void beforeEach() throws IOException {
        ObjectMapper mapper = new ObjectMapper()
                .registerModule(new JavaTimeModule());
        URL url = DatabaseSeeder.class.getResource("/customer-dev-schema.json");
        var customers = mapper
                .readValue(url, new TypeReference<List<CustomerDocument>>() {});
        this.customerRepository.insert(customers);
        url = DatabaseSeeder.class.getResource("/order-dev-schema.json");
        var orders = mapper
                .readValue(url, new TypeReference<List<OrderDocument>>() {});
        this.orderRepository.insert(orders);
    }

    @AfterEach
    void afterEach() {
        this.customerRepository.deleteAll();
        this.orderRepository.deleteAll();
    }

}
