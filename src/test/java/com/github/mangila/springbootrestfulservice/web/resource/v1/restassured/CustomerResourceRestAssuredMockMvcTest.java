package com.github.mangila.springbootrestfulservice.web.resource.v1.restassured;

import com.github.mangila.springbootrestfulservice.web.dto.v1.CustomerDto;
import com.github.mangila.springbootrestfulservice.web.resource.v1.CustomerResource;
import com.github.mangila.springbootrestfulservice.web.service.v1.CustomerService;
import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.module.mockmvc.response.MockMvcResponse;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.when;

/**
 * given() — specifies the HTTP request details
 * when() — specifies the HTTP verb as well as the route
 * then() — validates the HTTP response
 */
@Tag("restassured")
@WebMvcTest(CustomerResource.class)
public class CustomerResourceRestAssuredMockMvcTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CustomerService service;

    @BeforeEach
    void beforeEach() {
        RestAssuredMockMvc.mockMvc(this.mockMvc);
    }

    @Test
    void findAll() {
        when(this.service.findAll()).thenReturn(Lists.newArrayList(new CustomerDto()));

        MockMvcResponse response = RestAssuredMockMvc.given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(ContentType.JSON)
                .when()
                .get("v1/customer")
                .then()
                .status(HttpStatus.OK)
                .extract().response();
        CustomerDto[] body = response.getBody().as(CustomerDto[].class);
        Assertions.assertEquals(1, body.length);
    }

    @Test
    void findById() {
        String uuid = UUID.randomUUID().toString();
        CustomerDto c = new CustomerDto();
        c.setId(uuid);
        c.setName("Elrond");
        when(this.service.findById(uuid)).thenReturn(c);

        RestAssuredMockMvc.given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(ContentType.JSON)
                .when()
                .get("v1/customer/" + uuid)
                .then()
                .status(HttpStatus.OK)
                .body("id", equalTo(uuid))
                .body("name", equalTo("Elrond"));
    }

    @Test
    void insert() {
        String uuid = UUID.randomUUID().toString();
        CustomerDto c = new CustomerDto();
        c.setName("Galadriel");
        when(this.service.insert(c)).thenReturn(uuid);

        RestAssuredMockMvc.given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(ContentType.JSON)
                .body(c)
                .when()
                .post("v1/customer")
                .then()
                .status(HttpStatus.CREATED)
                .header(HttpHeaders.LOCATION, equalTo("/v1/customer/" + uuid));
    }

    @Test
    void update() {
        String uuid = UUID.randomUUID().toString();
        CustomerDto c = new CustomerDto();
        c.setName("Sauron");
        when(this.service.update(uuid, c)).thenReturn(uuid);

        RestAssuredMockMvc.given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(ContentType.JSON)
                .body(c)
                .when()
                .put("v1/customer/" + uuid)
                .then()
                .status(HttpStatus.NO_CONTENT)
                .header(HttpHeaders.CONTENT_LOCATION, equalTo("/v1/customer/" + uuid));
    }

    @Test
    void deleteById() {
        String uuid = UUID.randomUUID().toString();
        RestAssuredMockMvc.given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(ContentType.JSON)
                .body(uuid)
                .when()
                .delete("v1/customer/" + uuid)
                .then()
                .status(HttpStatus.NO_CONTENT);
    }

}
