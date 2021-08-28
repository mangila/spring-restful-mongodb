package com.github.mangila.springbootrestfulservice.web.resource.v1.springboottest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mangila.springbootrestfulservice.domain.CustomerDocument;
import com.github.mangila.springbootrestfulservice.web.dto.v1.CustomerDto;
import com.github.mangila.springbootrestfulservice.web.repository.v1.CustomerRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CustomerResourceSpringBootTest {

    @LocalServerPort
    private int port;

    private String customerUrl;

    private final List<String> testIds = new ArrayList<>();

    @Autowired
    private TestRestTemplate http;

    @Autowired
    private CustomerRepository repository;

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
                    this.testIds.add(id);
                });
    }

    @AfterEach
    void afterEach() {
        this.repository.deleteAll();
    }

    @Test
    void findAll() {
        ResponseEntity<CustomerDto[]> response = http.getForEntity(this.customerUrl, CustomerDto[].class);
        assertEquals(OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(4, response.getBody().length);
    }

    @Test
    void findById() {
        String url = this.customerUrl + "" + this.testIds.get(0);
        ResponseEntity<CustomerDto> response = http.getForEntity(url, CustomerDto.class);
        assertEquals(OK, response.getStatusCode());
        assertNotNull(response.getBody());
        CustomerDto customerDto = response.getBody();
        assertEquals("Idun", customerDto.getName());
    }

    @Test
    void insertAndThrowValidationErrors() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        CustomerDto c = new CustomerDto();
        ResponseEntity<String> response = http.postForEntity(this.customerUrl, c, String.class);
        assertEquals(BAD_REQUEST, response.getStatusCode());
        JsonNode jsonNode = mapper.readTree(response.getBody());
        String nameField = jsonNode.get("name").asText();
        assertEquals("must not be blank", nameField);

        c.setId("some id");
        c.setRegistration(LocalDate.now().plusDays(1));
        c.setName("Al");
        c.setOrderHistory(new ArrayList<>());
        response = http.postForEntity(this.customerUrl, c, String.class);
        assertEquals(BAD_REQUEST, response.getStatusCode());
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

    }

    @Test
    void deleteById() {

    }

}
