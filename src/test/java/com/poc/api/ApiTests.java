package com.poc.api;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.poc.utils.CustomExtentReporterManager;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import java.lang.invoke.MethodHandles;
import java.util.logging.Logger;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class ApiTests {

    private static final Logger logger = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());
    private static ExtentReports extent;
    private ExtentTest test;

    @BeforeAll
    public static void setUp() {
        Injector injector = Guice.createInjector( new CustomExtentReporterManager());
        extent = injector.getInstance(ExtentReports.class);
    }

    @BeforeEach
    public void setUpTest(TestInfo testInfo) {
        logger.info(" ** Starting test: " + testInfo.getDisplayName());
        test = extent.createTest(testInfo.getDisplayName());
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
}
