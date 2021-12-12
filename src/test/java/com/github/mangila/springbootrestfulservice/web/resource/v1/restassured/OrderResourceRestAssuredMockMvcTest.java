package com.github.mangila.springbootrestfulservice.web.resource.v1.restassured;

import com.github.mangila.springbootrestfulservice.domain.v1.Address;
import com.github.mangila.springbootrestfulservice.web.dto.v1.OrderDto;
import com.github.mangila.springbootrestfulservice.web.resource.v1.OrderResource;
import com.github.mangila.springbootrestfulservice.service.OrderService;
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
        when(this.service.findAll()).thenReturn(Lists.newArrayList(new OrderDto()));

        MockMvcResponse response = RestAssuredMockMvc.given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(ContentType.JSON)
                .when()
                .get("v1/order")
                .then()
                .status(HttpStatus.OK)
                .extract().response();
        OrderDto[] body = response.getBody().as(OrderDto[].class);
        Assertions.assertEquals(1, body.length);
    }

    @Test
    void findById() {
        String uuid = UUID.randomUUID().toString();
        OrderDto o = new OrderDto();
        o.setId(uuid);
        o.setAmount(750);
        Address a = new Address();
        a.setCity("South Park");
        a.setStreet("635 Avenue de Los Mexicanos");
        o.setAddress(a);
        o.setProducts(Lists.newArrayList("Onion", "Garlic Bread"));
        when(this.service.findById(uuid)).thenReturn(o);

        RestAssuredMockMvc.given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(ContentType.JSON)
                .when()
                .get("v1/order/" + uuid)
                .then()
                .status(HttpStatus.OK)
                .body("id", equalTo(uuid))
                .body("amount", equalTo(750))
                .body("address.city", equalTo("South Park"))
                .body("products[0]", equalTo("Onion"));
    }

    @Test
    void insert() {
        String orderId = UUID.randomUUID().toString();
        OrderDto o = new OrderDto();
        o.setAmount(750);
        Address a = new Address();
        a.setCity("South Park");
        a.setStreet("28201 E. Bonanza St.");
        o.setAddress(a);
        o.setProducts(Lists.newArrayList("iPad", "iPhone"));
        String customerId = UUID.randomUUID().toString();
        when(this.service.insert(customerId, o)).thenReturn(orderId);

        RestAssuredMockMvc.given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(ContentType.JSON)
                .body(o)
                .when()
                .post("v1/order/" + customerId)
                .then()
                .status(HttpStatus.CREATED)
                .header(HttpHeaders.LOCATION, equalTo("/v1/order/" + orderId));
    }

    @Test
    void deleteById() {
        String uuid = UUID.randomUUID().toString();
        RestAssuredMockMvc.given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(ContentType.JSON)
                .body(uuid)
                .when()
                .delete("v1/order/" + uuid)
                .then()
                .status(HttpStatus.NO_CONTENT);
    }

}
