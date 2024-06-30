package com.poc.api;

import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class ApiTests {

    @Test
    public void testGetRequest() {
        RestAssured.baseURI = "https://api.coindesk.com";
        given()
                .when()
                .get("/v1/bpi/currentprice.json")
                .then()
                .statusCode(200)
                .body("chartName", equalTo("Bitcoin"));
    }

    @Test
    public void getRequestLog(){
        RestAssured.baseURI = "https://api.coindesk.com";
        given()
                .when()
                .get("/v1/bpi/currentprice.json")
                .then()
                .log().body()
                .statusCode(200)
                .body("chartName", equalTo("Bitcoin"));
    }
}
