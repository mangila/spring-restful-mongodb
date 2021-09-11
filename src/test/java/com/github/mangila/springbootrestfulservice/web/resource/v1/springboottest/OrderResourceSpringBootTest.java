package com.github.mangila.springbootrestfulservice.web.resource.v1.springboottest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mangila.springbootrestfulservice.domain.Address;
import com.github.mangila.springbootrestfulservice.web.dto.v1.OrderDto;
import com.github.mangila.springbootrestfulservice.web.resource.v1.SeededEmbeddedMongo;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.http.HttpStatus.*;

@Tag("springboot")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OrderResourceSpringBootTest extends SeededEmbeddedMongo {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate http;

    private final String orderId = "2cdf9886-f565-4d5f-8bbc-2561806b2052";

    private final String customerId = "c8127464-3559-45ca-a70e-51f9c3a6d1c0";

    private String orderUrl;

    @BeforeEach
    void beforeEachSetOrderURL() {
        this.orderUrl = "http://localhost:" + port + "/api/v1/order/";
    }

    @Test
    void findAll() {
        ResponseEntity<OrderDto[]> response = this.http.getForEntity(this.orderUrl, OrderDto[].class);
        assertEquals(OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(11, response.getBody().length);
    }

    @Test
    void findById() {
        String url = this.orderUrl + this.orderId;
        ResponseEntity<OrderDto> response = this.http.getForEntity(url, OrderDto.class);
        assertEquals(OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void insert() {
        String url = this.orderUrl + this.customerId;
        OrderDto o = new OrderDto();
        o.setProducts(Lists.newArrayList("Book", "Socks", "Guitar"));
        Address a = new Address();
        a.setStreet("Uruk street 99");
        a.setCity("Midgard");
        o.setAddress(a);
        o.setAmount(3000);
        ResponseEntity<String> response = this.http.postForEntity(url, o, String.class);
        assertEquals(CREATED, response.getStatusCode());
        assertTrue(response.getHeaders().containsKey(LOCATION));
    }

    @Test
    void insertAndThrowValidationError() throws JsonProcessingException {
        String url = this.orderUrl + this.customerId;
        OrderDto o = new OrderDto();
        ResponseEntity<String> response = this.http.postForEntity(url, o, String.class);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(response.getBody());
        String amountField = jsonNode.get("amount").asText();
        assertEquals("must not be null", amountField);
        String addressField = jsonNode.get("address").asText();
        assertEquals("must not be null", addressField);
        String productsArray = jsonNode.get("products").asText();
        assertEquals("must not be null", productsArray);

        o.setAmount(-2000);
        response = this.http.postForEntity(url, o, String.class);
        jsonNode = mapper.readTree(response.getBody());
        amountField = jsonNode.get("amount").asText();
        assertEquals("must be greater than 0", amountField);
    }

    @Test
    void deleteById() {
        String url = this.orderUrl + this.orderId;
        ResponseEntity<String> deleteResponse = this.http.exchange(url, HttpMethod.DELETE, null, String.class);
        assertEquals(NO_CONTENT, deleteResponse.getStatusCode());

        ResponseEntity<String> getResponse = this.http.getForEntity(url, String.class);
        assertEquals(NOT_FOUND, getResponse.getStatusCode());
    }

}
