package com.github.mangila.springbootrestfulservice.web.resource.v1.restassured;

import com.github.mangila.springbootrestfulservice.web.dto.v1.CustomerDto;
import com.github.mangila.springbootrestfulservice.web.resource.v1.EmbeddedMongoDatabaseSeed;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * given() — specifies the HTTP request details
 * when() — specifies the HTTP verb as well as the route
 * then() — validates the HTTP response
 */
@Tag("restassured")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CustomerResourceRestAssuredTest extends EmbeddedMongoDatabaseSeed {

    @LocalServerPort
    private int port;

    private final String customerId = "c8127464-3559-45ca-a70e-51f9c3a6d1c0";

    @BeforeEach
    void beforeEachInitRestAssuredConfig() {
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
        assertEquals(body.length, 5);
    }

    @Test
    void findById() {
        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .get("customer/" + this.customerId)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("name", equalTo("Aragorn"));
    }

    @Test
    void insert() {
        CustomerDto c = new CustomerDto();
        c.setName("Gandalf");

        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .body(c)
                .post("customer")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .header(HttpHeaders.LOCATION, notNullValue());
    }

    @Test
    void update() {
        CustomerDto c = new CustomerDto();
        c.setName("Tom Bombadil");

       given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .body(c)
                .put("customer/" + this.customerId)
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value())
                .header(HttpHeaders.CONTENT_LOCATION,
                        equalTo("/api/v1/customer/" + this.customerId));
    }

    @Test
    void deleteById() {
        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .delete("customer/" + this.customerId)
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());

        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .get("customer/" + this.customerId)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());

    }

}
