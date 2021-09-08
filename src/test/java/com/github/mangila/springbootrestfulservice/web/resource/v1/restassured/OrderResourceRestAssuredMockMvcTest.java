package com.github.mangila.springbootrestfulservice.web.resource.v1.restassured;

import com.github.mangila.springbootrestfulservice.web.resource.v1.CustomerResource;
import com.github.mangila.springbootrestfulservice.web.resource.v1.OrderResource;
import com.github.mangila.springbootrestfulservice.web.service.v1.OrderService;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

/**
 * given() — specifies the HTTP request details
 * when() — specifies the HTTP verb as well as the route
 * then() — validates the HTTP response
 */
@Tag("restassured")
@WebMvcTest(OrderResource.class)
public class OrderResourceRestAssuredMockMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService service;

    @BeforeEach
    void beforeEach() {
        RestAssuredMockMvc.mockMvc(this.mockMvc);
    }

    @Test
    void findAll() {

    }

    @Test
    void findById() {

    }

    @Test
    void insert() {

    }

    @Test
    void deleteById() {

    }

}
