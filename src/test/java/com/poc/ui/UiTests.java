package com.poc.ui;

import com.aventstack.extentreports.ExtentTest;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.poc.utils.BaseTestEnvConfigurator;
import com.poc.utils.CustomWebDriverManager;
import com.poc.utils.ExtentUiTestWatcher;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.openqa.selenium.*;
import java.io.File;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.nio.file.Files;
import java.util.UUID;
import java.util.logging.Logger;

import static com.poc.utils.BaseTestEnvConfigurator.extent;
import static com.poc.utils.BaseTestEnvConfigurator.props;
import static org.assertj.core.api.Assertions.assertThat;


public class UiTests{
    public static final String BTN_ACCEPT_ALL = "//button[./div[text()='Accept all']]";
    public static final String BTN_LUCKY = "input[value*='Lucky']";
    private static final Logger logger = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());
    private ExtentTest extentTest;
    private WebDriver driver;

    @RegisterExtension
    static ExtentUiTestWatcher watcher;

    static {
        try {
            watcher = new ExtentUiTestWatcher();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeAll
    static void setUp() throws IOException {
        BaseTestEnvConfigurator.getConfigurator();
        watcher.setExtentTestReports(extent);
    }

    @BeforeEach
    public void setup(TestInfo testInfo) {
        extentTest = extent.createTest(testInfo.getDisplayName());
        watcher.setTest(extentTest);

        Injector injector = Guice.createInjector(new CustomWebDriverManager());
        driver = injector.getInstance(WebDriver.class);
        watcher.setDriver(driver);

        driver.get(props.getProperty("baseUrl"));
    }

    @Test
    @DisplayName("UI: smoke (Should PASSED...)")
    public void smoke() {
        assertThat(false).isFalse();
    }

    @Test
    @DisplayName("UI: simpleTest (Should PASSED...)")
    public void simpleTest() {
        getAcceptAllBtn().click();
        assertThat(driver.getTitle()).isEqualTo("Google");
        assertThat(getLuckyBtn().isEnabled()).isTrue();

        assertThat(getLuckyBtn().isEnabled()).isFalse();
    }

    @Test
    @DisplayName("UI: alwaysSkippedUiTest (Should SKIPPED...)")
    @Disabled
    public void alwaysSkippedUiTest() {
        // content of the test is not important because it will be skipped
        getAcceptAllBtn().click();
        assertThat(driver.getTitle()).isEqualTo("Google");
        assertThat(getLuckyBtn().isEnabled()).isTrue();
    }

    @Test
    @DisplayName("UI: alwaysFailingUiTest (Should FAILED...)")
    public void alwaysFailingUiTest() {
        driver.get("https://www.google.com");
        assertThat(true).isFalse();
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
            extentTest.addScreenCaptureFromPath(fullFileNamePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
