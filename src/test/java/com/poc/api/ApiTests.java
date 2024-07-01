package com.poc.api;

import com.aventstack.extentreports.ExtentTest;
import com.poc.utils.BaseTestEnvConfigurator;
import com.poc.utils.ExtentApiTestWatcher;
import io.restassured.RestAssured;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.logging.Logger;

import static com.poc.utils.BaseTestEnvConfigurator.extent;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class ApiTests{
    private static final Logger logger = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());
    public ExtentTest extentTest;

    @RegisterExtension
    static ExtentApiTestWatcher watcher = new ExtentApiTestWatcher();

    @BeforeAll
    static void setUp() throws IOException {
        BaseTestEnvConfigurator.getConfigurator();
        watcher.setExtentTestReports(extent);
    }

    @BeforeEach
    public void setUpTest(TestInfo testInfo) {
        logger.info(" ** Starting test: " + testInfo.getDisplayName());
        extentTest = extent.createTest(testInfo.getDisplayName());
        extent.flush();
        watcher.setTest(extentTest);
    }

    @AfterEach
    public void tearDownTest() {
        logger.info(" ** Test completed");
        extent.flush();
    }

    @Test
    @DisplayName("API:: testGetRequest (Should PASSED...)")
    public void testGetRequest() {
        extentTest.assignAuthor("John Doe");
        RestAssured.baseURI = "https://api.coindesk.com";
        given()
                .when()
                .get("/v1/bpi/currentprice.json")
                .then()
                .statusCode(200)
                .body("chartName", equalTo("Bitcoin"));
        logger.info(" - checking ConsoleLogger for 'testGetRequest'");
        extentTest.info("1 - checking ExtentLogger for 'testGetRequest'");
        extentTest.info("2 - checking ExtentLogger for 'testGetRequest'");
        extentTest.info("3 - checking ExtentLogger for 'testGetRequest'");
    }

    @Test
    @DisplayName("API:: failingApiTest (Should FAILED...)")
    public void failingApiTest() {
        extentTest.assignAuthor("Not Me");
        RestAssured.baseURI = "https://api.coindesk.com";
        given()
                .when()
                .get("/v1/bpi/currentprice.json")
                .then()
                .statusCode(200)
                .body("chartName", equalTo("Bitcoin"));
        assertThat(false).isTrue();
        logger.info(" That comment shouldn't be read becasue the test will fail earlier");
    }

    @Test
    @DisplayName("API:: getRequestLog (Should WARNED...)")
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
        extentTest.info("1 - checking ExtentLogger for 'getRequestLog'");
        extentTest.info("2 - checking ExtentLogger for 'getRequestLog'");
        extentTest.warning("3 - checking ExtentLogger for 'getRequestLog'");
        extentTest.info("4 - checking ExtentLogger for 'getRequestLog'");
    }
}
