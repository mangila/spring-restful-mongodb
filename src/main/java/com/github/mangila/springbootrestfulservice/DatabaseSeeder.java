package com.github.mangila.springbootrestfulservice;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.mangila.springbootrestfulservice.domain.CustomerDocument;
import com.github.mangila.springbootrestfulservice.domain.OrderDocument;
import com.github.mangila.springbootrestfulservice.web.repository.v1.CustomerRepository;
import com.github.mangila.springbootrestfulservice.web.repository.v1.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.List;

@Component
@Profile("dev")
@Slf4j
public class DatabaseSeeder implements InitializingBean {

    private final CustomerRepository customerRepository;

    private final OrderRepository orderRepository;

    @Autowired
    public DatabaseSeeder(CustomerRepository customerRepository, OrderRepository orderRepository) {
        this.customerRepository = customerRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    public void afterPropertiesSet() throws IOException {
        ObjectMapper mapper = new ObjectMapper()
                .registerModule(new JavaTimeModule());
        URL url = DatabaseSeeder.class.getResource("/customer-dev-schema.json");
        var customers = mapper
                .readValue(url, new TypeReference<List<CustomerDocument>>() {
                });
        customerRepository.insert(customers);
        url = DatabaseSeeder.class.getResource("/order-dev-schema.json");
        var orders = mapper
                .readValue(url, new TypeReference<List<OrderDocument>>() {});
        orderRepository.insert(orders);
        log.info("Database seeded with dev data.");
    }
}
