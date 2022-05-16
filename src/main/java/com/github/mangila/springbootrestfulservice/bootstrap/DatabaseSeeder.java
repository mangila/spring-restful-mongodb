package com.github.mangila.springbootrestfulservice.bootstrap;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mangila.springbootrestfulservice.persistence.domain.CustomerDocument;
import com.github.mangila.springbootrestfulservice.persistence.domain.OrderDocument;
import com.github.mangila.springbootrestfulservice.persistence.repository.CustomerRepository;
import com.github.mangila.springbootrestfulservice.persistence.repository.OrderRepository;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.List;

@Component
@Profile("dev")
public class DatabaseSeeder implements InitializingBean, DisposableBean {

    private final CustomerRepository customerRepository;

    private final OrderRepository orderRepository;

    private final ObjectMapper mapper;

    @Autowired
    public DatabaseSeeder(CustomerRepository customerRepository, OrderRepository orderRepository, ObjectMapper mapper) {
        this.customerRepository = customerRepository;
        this.orderRepository = orderRepository;
        this.mapper = mapper;
    }

    @Override
    public void afterPropertiesSet() throws IOException {
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

    @Override
    public void destroy() {
        this.customerRepository.deleteAll();
        this.orderRepository.deleteAll();
    }
}
