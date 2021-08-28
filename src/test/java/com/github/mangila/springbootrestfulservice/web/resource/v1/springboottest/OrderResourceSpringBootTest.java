package com.github.mangila.springbootrestfulservice.web.resource.v1.springboottest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mangila.springbootrestfulservice.domain.Address;
import com.github.mangila.springbootrestfulservice.domain.CustomerDocument;
import com.github.mangila.springbootrestfulservice.domain.OrderDocument;
import com.github.mangila.springbootrestfulservice.web.dto.v1.OrderDto;
import com.github.mangila.springbootrestfulservice.web.repository.v1.CustomerRepository;
import com.github.mangila.springbootrestfulservice.web.repository.v1.OrderRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.http.HttpStatus.*;

@Tag("springboot")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OrderResourceSpringBootTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate http;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private OrderRepository orderRepository;

    private String orderUrl;

    private String customerTestId;

    private final List<String> validOrderIds = new ArrayList<>();

    @BeforeEach
    void beforeEach() {
        this.orderUrl = "http://localhost:" + port + "/api/v1/order/";
        Stream.of("Erik")
                .forEach(name -> {
                    CustomerDocument c = new CustomerDocument();
                    c.setName(name);
                    c.setRegistration(LocalDate.of(
                            ThreadLocalRandom.current().nextInt(2010, 2020),
                            ThreadLocalRandom.current().nextInt(1, 12),
                            ThreadLocalRandom.current().nextInt(1, 28)
                    ));
                    c.setOrderHistory(new ArrayList<>());
                    this.customerTestId = this.customerRepository.insert(c).getId();
                    for (int i = 0; i < 10; i++) {
                        OrderDocument o = new OrderDocument();
                        o.setAmount(ThreadLocalRandom.current().nextInt(200, 800));
                        o.setProducts(this.getRandomProducts());
                        o.setAddress(new Address(
                                "Asgard road " + ThreadLocalRandom.current().nextInt(1, 100),
                                "Asgard"));
                        String orderId = orderRepository.insert(o).getId();
                        c = customerRepository.findByName(name);
                        c.getOrderHistory().add(orderId);
                        validOrderIds.add(orderId);
                        customerRepository.save(c);
                    }
                });
    }

    private List<String> getRandomProducts() {
        List<String> products = List.of("T-Shirt", "Jeans", "Sweatpants",
                "Trousers", "Strawberries", "Meatballs",
                "Milk", "Watermelon", "Salomon", "Red Meat");
        var l = new ArrayList<String>();
        for (int i = 0; i <= 5; i++) {
            l.add(products.get(ThreadLocalRandom.current().nextInt(0, 9)));
        }
        return l;
    }

    @AfterEach
    void afterEach() {
        this.customerRepository.deleteAll();
        this.orderRepository.deleteAll();
    }

    @Test
    void findAll() {
        ResponseEntity<OrderDto[]> response = this.http.getForEntity(this.orderUrl, OrderDto[].class);
        assertEquals(OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(10, response.getBody().length);
    }

    @Test
    void findById() {
        String url = this.orderUrl + this.validOrderIds.get(0);
        ResponseEntity<OrderDto> response = this.http.getForEntity(url, OrderDto.class);
        assertEquals(OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void insert() {
        String url = this.orderUrl + this.customerTestId;
        OrderDto o = new OrderDto();
        o.setProducts(this.getRandomProducts());
        o.setAddress(new Address());
        o.setAmount(3000);
        ResponseEntity<String> response = this.http.postForEntity(url, o, String.class);
        assertEquals(CREATED, response.getStatusCode());
        assertTrue(response.getHeaders().containsKey(LOCATION));
    }

    @Test
    void insertAndThrowValidationError() throws JsonProcessingException {
        String url = this.orderUrl + this.customerTestId;
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
        String url = this.orderUrl + this.validOrderIds.get(0);
        ResponseEntity<String> deleteResponse = this.http.exchange(url, HttpMethod.DELETE, null, String.class);
        assertEquals(NO_CONTENT, deleteResponse.getStatusCode());

        ResponseEntity<String> getResponse = this.http.getForEntity(url, String.class);
        assertEquals(NOT_FOUND, getResponse.getStatusCode());
    }

}
