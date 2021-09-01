package com.github.mangila.springbootrestfulservice.web.resource.v1.restassured;

import com.github.mangila.springbootrestfulservice.web.dto.v1.CustomerDto;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import java.util.Arrays;

import static io.restassured.RestAssured.given;

@Tag("restassured")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CustomerResourceRestAssuredTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void afterEach() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = this.port;
        RestAssured.basePath = "/api/v1";
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
    }

    @Test
    void findById() {

    }

    @Test
    void update() {

    }

    @Test
    void deleteById() {

    }

}
