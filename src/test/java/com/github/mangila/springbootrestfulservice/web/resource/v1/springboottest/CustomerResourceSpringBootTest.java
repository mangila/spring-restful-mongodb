package com.github.mangila.springbootrestfulservice.web.resource.v1.springboottest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mangila.springbootrestfulservice.web.dto.v1.CustomerDto;
import com.github.mangila.springbootrestfulservice.web.resource.v1.SeededEmbeddedMongo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpHeaders.CONTENT_LOCATION;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.http.HttpStatus.*;

@Tag("springboot")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CustomerResourceSpringBootTest extends SeededEmbeddedMongo {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate http;

    private final String customerId = "c8127464-3559-45ca-a70e-51f9c3a6d1c0";

    private String customerURL;

    @Autowired
    private CacheManager cacheManager;

    @BeforeEach
    void beforeEachSetCustomerURL() {
        this.customerURL = "http://localhost:" + port + "/api/v1/customer/";
    }

    @AfterEach
    void afterEachClearCache() {
        this.cacheManager.getCacheNames()
                .forEach(c -> cacheManager.getCache(c).clear());
    }

    @Test
    void findAll() {
        ResponseEntity<CustomerDto[]> response = this.http.getForEntity(this.customerURL, CustomerDto[].class);
        assertEquals(OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(5, response.getBody().length);
    }

    @Test
    void findById() {
        String url = this.customerURL + this.customerId;
        ResponseEntity<CustomerDto> response = this.http.getForEntity(url, CustomerDto.class);
        assertEquals(OK, response.getStatusCode());
        assertNotNull(response.getBody());
        CustomerDto customerDto = response.getBody();
        assertEquals("Aragorn", customerDto.getName());
    }

    @Test
    void insert() {
        CustomerDto c = new CustomerDto();
        c.setName("Saruman");
        ResponseEntity<String> response = this.http.postForEntity(this.customerURL, c, String.class);
        assertEquals(CREATED, response.getStatusCode());
        assertTrue(response.getHeaders().containsKey(LOCATION));
    }

    @Test
    void insertAndThrowValidationErrors() throws JsonProcessingException {
        CustomerDto c = new CustomerDto();
        ResponseEntity<String> response = this.http.postForEntity(this.customerURL, c, String.class);
        assertEquals(BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(response.getBody());
        String nameField = jsonNode.get("name").asText();
        assertEquals("must not be blank", nameField);

        c.setId("some id");
        c.setRegistration(LocalDate.now());
        c.setName("Al");
        c.setOrderHistory(new ArrayList<>());
        response = this.http.postForEntity(this.customerURL, c, String.class);
        assertEquals(BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());

        jsonNode = mapper.readTree(response.getBody());
        nameField = jsonNode.get("name").asText();
        assertEquals("size must be between 3 and 100", nameField);
        String orderHistoryField = jsonNode.get("orderHistory").asText();
        assertEquals("must be null", orderHistoryField);
        String registrationField = jsonNode.get("registration").asText();
        assertEquals("must be null", registrationField);
        String idField = jsonNode.get("id").asText();
        assertEquals("must be null", idField);
    }

    @Test
    void updateCustomer() {
        String url = this.customerURL + this.customerId;
        ResponseEntity<CustomerDto> getResponse = this.http.getForEntity(url, CustomerDto.class);
        assertEquals(OK, getResponse.getStatusCode());
        assertNotNull(getResponse.getBody());
        CustomerDto c = getResponse.getBody();
        assertEquals("Aragorn", c.getName());

        c = new CustomerDto();
        c.setName("Gollum");
        HttpEntity<CustomerDto> httpEntity = new HttpEntity<>(c, null);
        ResponseEntity<String> putResponse = this.http.exchange(url, HttpMethod.PUT, httpEntity, String.class);
        assertEquals(NO_CONTENT, putResponse.getStatusCode());
        assertTrue(putResponse.getHeaders().containsKey(CONTENT_LOCATION));

        getResponse = this.http.getForEntity(url, CustomerDto.class);
        assertEquals(OK, getResponse.getStatusCode());
        assertNotNull(getResponse.getBody());
        c = getResponse.getBody();
        assertEquals("Gollum", c.getName());
    }

    @Test
    void deleteById() {
        String url = this.customerURL + this.customerId;
        ResponseEntity<String> deleteResponse = this.http.exchange(url, HttpMethod.DELETE, null, String.class);
        assertEquals(NO_CONTENT, deleteResponse.getStatusCode());

        ResponseEntity<String> getResponse = this.http.getForEntity(url, String.class);
        assertEquals(NOT_FOUND, getResponse.getStatusCode());
    }

}
