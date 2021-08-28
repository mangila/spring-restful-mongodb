package com.github.mangila.springbootrestfulservice.web.resource.v1.springboottest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mangila.springbootrestfulservice.domain.CustomerDocument;
import com.github.mangila.springbootrestfulservice.web.dto.v1.CustomerDto;
import com.github.mangila.springbootrestfulservice.web.repository.v1.CustomerRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpHeaders.CONTENT_LOCATION;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.http.HttpStatus.*;

@Tag("springboot")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CustomerResourceSpringBootTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate http;

    @Autowired
    private CustomerRepository repository;

    private String customerUrl;

    private final List<String> validCustomerIds = new ArrayList<>();

    @BeforeEach
    void beforeEach() {
        this.customerUrl = "http://localhost:" + port + "/api/v1/customer/";
        Stream.of("Idun", "Heimdall", "Magne", "Mode")
                .forEach(name -> {
                    CustomerDocument c = new CustomerDocument();
                    c.setName(name);
                    c.setRegistration(LocalDate.of(
                            ThreadLocalRandom.current().nextInt(2010, 2020),
                            ThreadLocalRandom.current().nextInt(1, 12),
                            ThreadLocalRandom.current().nextInt(1, 28)
                    ));
                    c.setOrderHistory(new ArrayList<>());
                    String id = this.repository.insert(c).getId();
                    this.validCustomerIds.add(id);
                });
    }

    @AfterEach
    void afterEach() {
        this.repository.deleteAll();
    }

    @Test
    void findAll() {
        ResponseEntity<CustomerDto[]> response = this.http.getForEntity(this.customerUrl, CustomerDto[].class);
        assertEquals(OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(4, response.getBody().length);
    }

    @Test
    void findById() {
        String url = this.customerUrl + this.validCustomerIds.get(0);
        ResponseEntity<CustomerDto> response = this.http.getForEntity(url, CustomerDto.class);
        assertEquals(OK, response.getStatusCode());
        assertNotNull(response.getBody());
        CustomerDto customerDto = response.getBody();
        assertEquals("Idun", customerDto.getName());
    }

    @Test
    void insert() {
        CustomerDto c = new CustomerDto();
        c.setName("Tyr");
        ResponseEntity<String> response = this.http.postForEntity(this.customerUrl, c, String.class);
        assertEquals(CREATED, response.getStatusCode());
        assertTrue(response.getHeaders().containsKey(LOCATION));
    }

    @Test
    void insertAndThrowValidationErrors() throws JsonProcessingException {
        CustomerDto c = new CustomerDto();
        ResponseEntity<String> response = this.http.postForEntity(this.customerUrl, c, String.class);
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
        response = this.http.postForEntity(this.customerUrl, c, String.class);
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
        String url = this.customerUrl + this.validCustomerIds.get(0);
        ResponseEntity<CustomerDto> getResponse = this.http.getForEntity(url, CustomerDto.class);
        assertEquals(OK, getResponse.getStatusCode());
        assertNotNull(getResponse.getBody());
        CustomerDto c = getResponse.getBody();
        assertEquals("Idun", c.getName());

        c = new CustomerDto();
        c.setName("Brage");
        HttpEntity<CustomerDto> httpEntity = new HttpEntity<>(c, null);
        ResponseEntity<String> putResponse = this.http.exchange(url, HttpMethod.PUT, httpEntity, String.class);
        assertEquals(NO_CONTENT, putResponse.getStatusCode());
        assertTrue(putResponse.getHeaders().containsKey(CONTENT_LOCATION));

        getResponse = this.http.getForEntity(url, CustomerDto.class);
        assertEquals(OK, getResponse.getStatusCode());
        assertNotNull(getResponse.getBody());
        c = getResponse.getBody();
        assertEquals("Brage", c.getName());
    }

    @Test
    void deleteById() {
        String url = this.customerUrl + this.validCustomerIds.get(0);
        ResponseEntity<String> deleteResponse = this.http.exchange(url, HttpMethod.DELETE, null, String.class);
        assertEquals(NO_CONTENT, deleteResponse.getStatusCode());

        ResponseEntity<String> getResponse = this.http.getForEntity(url, String.class);
        assertEquals(NOT_FOUND, getResponse.getStatusCode());
    }

}
