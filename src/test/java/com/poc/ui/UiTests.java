package com.poc.ui;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;


public class UiTests {
    public static final String BTN_ACCEPT_ALL = "//button[./div[text()='Accept all']]";
    public static final String BTN_LUCKY = "input[value*='Lucky']";
    private WebDriver driver;
    private final Properties props = new Properties();

    @BeforeEach
    public void setup() throws IOException {
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
                WebDriverManager.chromedriver().browserVersion("126.0.6478.127").setup();
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
        driver.get(props.getProperty("baseUrl"));
    }

    @Test
    public void simpleTest() {
        getAcceptAllBtn().click();
        assertThat(driver.getTitle()).isEqualTo("Google");
        assertThat(getLuckyBtn().isEnabled()).isTrue();
    }

    private WebElement getLuckyBtn() {
        return driver.findElement(By.cssSelector(BTN_LUCKY));
    }

    private WebElement getAcceptAllBtn() {
        return driver.findElement(By.xpath(BTN_ACCEPT_ALL));
    }

    @AfterEach
    public void tearDown() {
        driver.quit();
    }
}
