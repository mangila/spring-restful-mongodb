package com.github.mangila.springbootrestfulservice.web.resource.v1.restassured;

import com.github.mangila.springbootrestfulservice.domain.CustomerDocument;
import com.github.mangila.springbootrestfulservice.web.dto.v1.CustomerDto;
import com.github.mangila.springbootrestfulservice.web.repository.v1.CustomerRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
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
import java.util.ArrayList;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Tag("restassured")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CustomerResourceRestAssuredTest {

    @LocalServerPort
    private int port;

    @Autowired
    private CustomerRepository repository;

    private String testId;

    @BeforeEach
    void beforeEach() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = this.port;
        RestAssured.basePath = "/api/v1";

        CustomerDocument c = new CustomerDocument();
        c.setName("Heimdal");
        c.setRegistration(LocalDate.now());
        c.setOrderHistory(new ArrayList<>());
        this.testId = repository.insert(c).getId();
    }

    @AfterEach
    void afterEach() {
        this.repository.deleteAll();
    }

    @Test
    void findAll() {
        Response response = given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .get("customer")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract().response();
        CustomerDto[] body = response.getBody().as(CustomerDto[].class);
        assertEquals(body.length, 1);
    }

    @Test
    void findById() {
        Response response = given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .get("customer/" + this.testId)
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract().response();
        CustomerDto body = response.getBody().as(CustomerDto.class);
        assertEquals(body.getName(), "Heimdal");
    }

    @Test
    void insert() {
        CustomerDto c = new CustomerDto();
        c.setName("Thor");

        Response response = given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .body(c)
                .post("customer")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract().response();
        String location = response.getHeaders()
                .getValue(HttpHeaders.LOCATION);
        assertNotNull(location);
    }

    @Test
    void deleteById() {
        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .delete("customer/" + this.testId)
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value())

        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .get("customer/" + this.testId)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());

    }

}
