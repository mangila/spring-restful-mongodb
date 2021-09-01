package com.github.mangila.springbootrestfulservice.web.resource.v1.restassured;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;

@Tag("restassured")
@SpringBootTest
public class OrderResourceRestAssuredTest {

    @BeforeEach
    public void beforeEach() {

    }



}
