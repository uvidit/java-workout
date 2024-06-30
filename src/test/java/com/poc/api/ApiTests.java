package com.poc.api;

import com.aventstack.extentreports.ExtentTest;
import com.poc.utils.BaseTestEnvConfigurator;
import io.restassured.RestAssured;
import org.junit.jupiter.api.*;

import java.lang.invoke.MethodHandles;
import java.util.logging.Logger;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class ApiTests {

    private static final Logger logger = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());
    private ExtentTest test;

    @BeforeAll
    public static void setUp() {
        BaseTestEnvConfigurator.getConfigurator();
    }

    @BeforeEach
    public void setUpTest(TestInfo testInfo) {
        logger.info(" ** Starting test: " + testInfo.getDisplayName());
        test = BaseTestEnvConfigurator.extent.createTest(testInfo.getDisplayName());
    }

    @Test
    public void testGetRequest() {
        RestAssured.baseURI = "https://api.coindesk.com";
        given()
                .when()
                .get("/v1/bpi/currentprice.json")
                .then()
                .statusCode(200)
                .body("chartName", equalTo("Bitcoin"));
        logger.info(" - checking ConsoleLogger for 'testGetRequest'");
        test.info(" - checking ExtentLogger for 'testGetRequest'");
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
        logger.info(" - checking ConsoleLogger for 'getRequestLog'");
        test.info(" - checking ExtentLogger for 'getRequestLog'");
    }

    @AfterEach
    public void tearDown() {
        BaseTestEnvConfigurator.extent.flush();
    }
}
