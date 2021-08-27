package com.github.mangila.springbootrestfulservice.web.resource.v1.spring;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mangila.springbootrestfulservice.web.dto.v1.CustomerDto;
import com.github.mangila.springbootrestfulservice.web.resource.v1.CustomerResource;
import com.github.mangila.springbootrestfulservice.web.service.v1.CustomerService;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpHeaders.CONTENT_LOCATION;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerResource.class)
class CustomerResourceWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService service;

    @Test
    void findAll() throws Exception {
        when(this.service.findAll()).thenReturn(Lists.newArrayList(new CustomerDto()));

        this.mockMvc.perform(get("/v1/customer").accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)));

    }

    @Test
    void findById() throws Exception {
        when(this.service.findById("123")).thenReturn(new CustomerDto());

        this.mockMvc.perform(get("/v1/customer/123")
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .json("{\"id\":null,\"name\":null,\"registration\":null,\"orderHistory\":null}"));

    }

    @Test
    void insertNewCustomer() throws Exception {
        CustomerDto customerDto = new CustomerDto();
        customerDto.setName("Frej");
        when(this.service.insert(customerDto)).thenReturn("123");

        this.mockMvc.perform(post("/v1/customer")
                        .contentType(APPLICATION_JSON)
                        .content(new ObjectMapper()
                                .writeValueAsString(customerDto)))
                .andExpect(status().isCreated())
                .andExpect(header().string(LOCATION, "/v1/customer/123"));
    }

    @Test
    void updateCustomer() throws Exception {
        CustomerDto customerDto = new CustomerDto();
        customerDto.setName("Frigg");
        String uuid = UUID.randomUUID().toString();
        when(this.service.update(uuid, customerDto)).thenReturn(uuid);

        this.mockMvc.perform(put("/v1/customer/" + uuid)
                        .contentType(APPLICATION_JSON)
                        .content(new ObjectMapper()
                                .writeValueAsString(customerDto)))
                .andExpect(status().isNoContent())
                .andExpect(header().string(CONTENT_LOCATION, "/v1/customer/" + uuid));
    }

    @Test
    void deleteById() {


    }
}