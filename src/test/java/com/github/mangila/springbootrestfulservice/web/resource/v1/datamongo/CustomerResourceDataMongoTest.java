package com.github.mangila.springbootrestfulservice.web.resource.v1.datamongo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mangila.springbootrestfulservice.web.dto.v1.CustomerDto;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpHeaders.CONTENT_LOCATION;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Tag("datamongo")
@DataMongoTest
public class CustomerResourceDataMongoTest {

    @Test
    void findAll() {
        //TODO impl
    }

    @Test
    void findById() {
        //TODO impl
    }

    @Test
    void insertNewCustomer() {
        //TODO impl
    }

    @Test
    void updateCustomer() {
        //TODO impl
    }

    @Test
    void deleteById() {
        //TODO impl
    }

}
