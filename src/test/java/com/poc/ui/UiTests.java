package com.poc.ui;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.poc.utils.BaseTestEnvConfigurator;
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

import static com.poc.utils.BaseTestEnvConfigurator.props;
import static com.poc.utils.BaseTestEnvConfigurator.extent;
import static org.assertj.core.api.Assertions.assertThat;


public class UiTests {
    public static final String BTN_ACCEPT_ALL = "//button[./div[text()='Accept all']]";
    public static final String BTN_LUCKY = "input[value*='Lucky']";
    private static final Logger logger = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());
    private WebDriver driver;

    private ExtentTest test;

    @BeforeAll
    public static void setUpReport() throws IOException {
        BaseTestEnvConfigurator.getConfigurator();
        // Create screenshots directory if it doesn't exist
        Path screenshotsDir = Paths.get("target/screenshots");
        if (!Files.exists(screenshotsDir)) {
            try {
                Files.createDirectories(screenshotsDir);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @BeforeEach
    public void setup(TestInfo testInfo) {
        test = BaseTestEnvConfigurator.extent.createTest(testInfo.getDisplayName());
        Injector injector = Guice.createInjector( new CustomWebDriverManager());
        driver = injector.getInstance(WebDriver.class);

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
        BaseTestEnvConfigurator.extent.flush();
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
