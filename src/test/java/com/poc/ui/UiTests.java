package com.poc.ui;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.Status;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;


public class UiTests {
    public static final String BTN_ACCEPT_ALL = "//button[./div[text()='Accept all']]";
    public static final String BTN_LUCKY = "input[value*='Lucky']";
    private WebDriver driver;
    private final Properties props = new Properties();

    private static ExtentReports extent;
    private ExtentTest test;

    @BeforeAll
    public static void setUpReport() {
        ExtentSparkReporter sparkReporter = new ExtentSparkReporter("extent-report.html");
        extent = new ExtentReports();
        extent.attachReporter(sparkReporter);
    }

    @BeforeEach
    public void setup(TestInfo testInfo) throws IOException {
        test = extent.createTest(testInfo.getDisplayName());

        FileInputStream configFile = new FileInputStream(
                String.format("src/test/resources/%s.properties"
//                        , System.getProperty("env", "local-chrome")));
                        , System.getProperty("env", "local-firefox")));
        props.load(configFile);
        switch (props.getProperty("browser")) {
            case "chrome" -> {
                WebDriverManager.chromedriver().browserVersion("126.0.6478.127").setup();
                ChromeOptions options = new ChromeOptions();
                options.addArguments("--lang=en-US");
                options.addArguments("--start-maximized");
                driver = new ChromeDriver(); }
            case "headless-chrome" -> {
                WebDriverManager.chromedriver().setup();
                ChromeOptions options = new ChromeOptions();
                options.addArguments("--headless");
                options.addArguments("--disable-gpu");
                options.addArguments("--window-size=1920,1080");
                driver = new ChromeDriver(options); }
            case "firefox" -> {
                WebDriverManager.firefoxdriver().setup();
                FirefoxProfile profile = new FirefoxProfile();
                profile.setPreference("intl.accept_languages", "en-US");
                profile.setPreference("general.useragent.locale", "en-US");
                profile.setPreference("intl.locale.requested", "en-US");
                profile.setPreference("intl.locale.matchOS", "false");
                profile.setPreference("browser.fixup.alternate.enabled", false);
                profile.setPreference("geo.enabled", false);
                profile.setPreference("geo.provider.use_gpsd", false);
                profile.setPreference("geo.prompt.testing", false);
                profile.setPreference("geo.prompt.testing.allow", false);
                FirefoxOptions options = new FirefoxOptions();
                options.setProfile(profile);
                options.addArguments("--start-maximized");
                driver = new FirefoxDriver();
            }
            default -> throw new IllegalArgumentException("Invalid browser");
        }
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
            Files.copy(screenshot.toPath(), new File("target/screenshots/" + name + ".png").toPath());
            test.addScreenCaptureFromPath("target/screenshots/" + name + ".png");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
