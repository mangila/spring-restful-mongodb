package com.github.mangila.springbootrestfulservice.web.resource.v1.restassured;

import com.github.mangila.springbootrestfulservice.domain.Address;
import com.github.mangila.springbootrestfulservice.web.dto.v1.OrderDto;
import com.github.mangila.springbootrestfulservice.web.resource.v1.EmbeddedMongoDatabaseSeed;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.assertj.core.util.Lists;
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

/**
 * given() — specifies the HTTP request details
 * when() — specifies the HTTP verb as well as the route
 * then() — validates the HTTP response
 */
@Tag("restassured")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OrderResourceRestAssuredTest extends EmbeddedMongoDatabaseSeed {

    @LocalServerPort
    private int port;

    private final String customerId = "c8127464-3559-45ca-a70e-51f9c3a6d1c0";

    private final String orderId = "2cdf9886-f565-4d5f-8bbc-2561806b2052";

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
                .get("order")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract().response();
        OrderDto[] body = response.getBody().as(OrderDto[].class);
        assertEquals(body.length, 11);
    }

    @Test
    void findById() {
        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .get("order/" + this.orderId)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("amount", equalTo(750))
                .body("address.street", equalTo("Minas Tirith road 101"))
                .body("address.city", equalTo("Gondor"));
    }

    @Test
    void insert() {
        OrderDto o = new OrderDto();
        o.setAmount(700);
        o.setProducts(Lists.newArrayList("Bread", "Ham", "Butter"));
        Address a = new Address();
        a.setStreet("Orc Street 102");
        a.setCity("Mordor");
        o.setAddress(a);

        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .body(o)
                .post("order/" + this.customerId)
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .header(HttpHeaders.LOCATION, notNullValue());
    }

    @Test
    void delete() {
        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .delete("order/" + this.orderId)
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
        assertEquals(body.length, 10);
    }

}
