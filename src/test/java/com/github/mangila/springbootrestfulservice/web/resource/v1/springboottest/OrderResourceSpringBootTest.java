package com.github.mangila.springbootrestfulservice.web.resource.v1.springboottest;

import com.github.mangila.springbootrestfulservice.web.repository.v1.OrderRepository;
import org.junit.jupiter.api.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

@Tag("springboot")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OrderResourceSpringBootTest {

    @LocalServerPort
    private int port;

    private String orderUrl;

    @Autowired
    private TestRestTemplate http;

    @Autowired
    private OrderRepository repository;
}
