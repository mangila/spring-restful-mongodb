package com.github.mangila.springbootrestfulservice.web.resource.v1.spring;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mangila.springbootrestfulservice.domain.Address;
import com.github.mangila.springbootrestfulservice.web.dto.v1.OrderDto;
import com.github.mangila.springbootrestfulservice.web.resource.v1.OrderResource;
import com.github.mangila.springbootrestfulservice.web.service.v1.OrderService;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderResource.class)
class OrderResourceWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService service;

    @Test
    void findAll() throws Exception {
        when(this.service.findAll()).thenReturn(Lists.newArrayList(new OrderDto()));

        this.mockMvc.perform(get("/v1/order").accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void findById() throws Exception {
        when(this.service.findById("12345")).thenReturn(new OrderDto());

        this.mockMvc.perform(get("/v1/order/12345")
                        .accept(APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(
                        content()
                                .json(
                                        "{\"id\":null," +
                                                "\"products\":null," +
                                                "\"amount\":null," +
                                                "\"address\":null}",
                                        true));
    }

    @Test
    void insertNewOrder() throws Exception {
        OrderDto orderDto = new OrderDto();
        orderDto.setAddress(new Address());
        orderDto.setAmount(2000);
        orderDto.setProducts(Lists.newArrayList());
        when(this.service.insert("123", orderDto)).thenReturn("123");

        this.mockMvc.perform(post("/v1/order/123")
                        .contentType(APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(orderDto)))
                .andExpect(status().isCreated())
                .andExpect(header().string(LOCATION, "http://localhost/v1/order/123"));
    }

    @Test
    void deleteById() throws Exception {
        this.mockMvc.perform(delete("/v1/order/123")
                        .contentType(APPLICATION_JSON)
                        .content("{\"id\":123}"))
                .andExpect(status().isNoContent());
    }
}