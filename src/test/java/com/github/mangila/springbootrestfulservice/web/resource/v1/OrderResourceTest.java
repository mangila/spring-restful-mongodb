package com.github.mangila.springbootrestfulservice.web.resource.v1;

import com.github.mangila.springbootrestfulservice.web.service.v1.OrderService;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@Tag("resources")
@WebMvcTest(OrderResource.class)
class OrderResourceTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService service;

    @Test
    void findAll() {
        //TODO impl
    }

    @Test
    void findById() {
        //TODO impl
    }

    @Test
    void insertNewOrder() {
        //TODO impl
    }

    @Test
    void deleteById() {
        //TODO impl
    }
}