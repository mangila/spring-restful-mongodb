package com.github.mangila.springbootrestfulservice.web.resource.v1.restassured;

import com.github.mangila.springbootrestfulservice.domain.Address;
import com.github.mangila.springbootrestfulservice.domain.CustomerDocument;
import com.github.mangila.springbootrestfulservice.domain.OrderDocument;
import com.github.mangila.springbootrestfulservice.web.dto.v1.OrderDto;
import com.github.mangila.springbootrestfulservice.web.repository.v1.CustomerRepository;
import com.github.mangila.springbootrestfulservice.web.repository.v1.OrderRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Tag("restassured")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OrderResourceRestAssuredTest {

    @LocalServerPort
    private int port;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CustomerRepository customerRepository;

    private String customerTestId;

    private String orderTestId;

    @BeforeEach
    void beforeEach() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = this.port;
        RestAssured.basePath = "/api/v1";

        CustomerDocument c = new CustomerDocument();
        c.setName("Kungen");
        c.setOrderHistory(Lists.emptyList());
        c.setRegistration(LocalDate.now());
        this.customerTestId = this.customerRepository.insert(c).getId();

        OrderDocument o = new OrderDocument();
        o.setProducts(Lists.newArrayList("Milk", "Creme Fraiche", "Wrangler Jeans"));
        o.setAmount(750);
        Address a = new Address();
        a.setCity("Stockholm");
        a.setStreet("Stockholmsvägen 101");
        o.setAddress(a);
        this.orderTestId = orderRepository.insert(o).getId();
    }

    @AfterEach
    void afterEach() {
        this.orderRepository.deleteAll();
    }

    @Test
    void findAll() {
        Response response = given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .get("order")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract().response();
        OrderDto[] body = response.getBody().as(OrderDto[].class);
        assertEquals(body.length, 1);
    }

    @Test
    void findById() {
        Response response = given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .get("order/" + this.orderTestId)
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract().response();
        OrderDto body = response.getBody().as(OrderDto.class);
        assertEquals("Stockholm", body.getAddress().getCity());
    }

    @Test
    void insert() {
        OrderDto o = new OrderDto();
        o.setAmount(700);
        o.setProducts(Lists.newArrayList("Bread", "Ham", "Butter"));
        Address a = new Address();
        a.setStreet("Paradisäppelvägen 102");
        a.setCity("Ankeborg");
        o.setAddress(a);

        Response response = given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .body(o)
                .post("order/" + this.customerTestId)
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract().response();
        String location = response.getHeaders()
                .getValue(HttpHeaders.LOCATION);
        assertNotNull(location);

        response = given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .get("order")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract().response();
        OrderDto[] body = response.getBody().as(OrderDto[].class);
        assertEquals(body.length, 2);

    }

    @Test
    void delete() {
        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .delete("order/" + this.orderTestId)
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());


        Response response = given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .get("order")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract().response();
        OrderDto[] body = response.getBody().as(OrderDto[].class);
        assertEquals(body.length, 0);
    }

}
