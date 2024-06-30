package com.poc.ui;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.poc.utils.CustomExtentReporterManager;
import com.poc.utils.CustomWebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.UUID;
import java.util.logging.Logger;

import static org.assertj.core.api.Assertions.assertThat;


public class UiTests {
    public static final String BTN_ACCEPT_ALL = "//button[./div[text()='Accept all']]";
    public static final String BTN_LUCKY = "input[value*='Lucky']";
    private static final Logger logger = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());
    private WebDriver driver;
    private final Properties props = new Properties();

    private static ExtentReports extent;
    private ExtentTest test;

    @BeforeAll
    public static void setUpReport() {
        Injector injector = Guice.createInjector( new CustomExtentReporterManager());
        extent = injector.getInstance(ExtentReports.class);
    }

    @BeforeEach
    public void setup(TestInfo testInfo) throws IOException {
        test = extent.createTest(testInfo.getDisplayName());
        Injector injector = Guice.createInjector( new CustomWebDriverManager());
        driver = injector.getInstance(WebDriver.class);


        FileInputStream configFile = new FileInputStream(
                String.format("src/test/resources/%s.properties"
//                        , System.getProperty("env", "local-chrome")));
                        , System.getProperty("env", "local-firefox")));
        props.load(configFile);
        // Create screenshots directory if it doesn't exist
        Path screenshotsDir = Paths.get("target/screenshots");
        if (!Files.exists(screenshotsDir)) {
            try {
                Files.createDirectories(screenshotsDir);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        driver.get(props.getProperty("baseUrl"));
    }

    @Test
    @DisplayName("Test Google search Smoke Test")
    public void simpleTest() {
        test.log(Status.INFO, "Navigated to Google");

        getAcceptAllBtn().click();
        assertThat(driver.getTitle()).isEqualTo("Google");
        assertThat(getLuckyBtn().isEnabled()).isTrue();

        assertThat(getLuckyBtn().isEnabled()).isFalse();
    }

    private WebElement getLuckyBtn() {
        return driver.findElement(By.cssSelector(BTN_LUCKY));
    }

    private WebElement getAcceptAllBtn() {
        return driver.findElement(By.xpath(BTN_ACCEPT_ALL));
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            takeScreenshot("Final state of test");
            driver.quit();
        }
        extent.flush();
    }

    public void takeScreenshot(String name) {
        File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        try {
            String fullFileNamePath = String.format("target/screenshots/%s-%s.png", name, UUID.randomUUID());
            Files.copy(screenshot.toPath(), new File(fullFileNamePath).toPath());
            test.addScreenCaptureFromPath(fullFileNamePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
