package com.poc.utils;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;

public class CustomWebDriverManager extends AbstractModule {
    @Provides
    public WebDriver provideWebDriver () {
        String browser = System.getProperty("browser", "chrome");

        switch (browser) {
            case "chrome" -> {
                WebDriverManager.chromedriver().browserVersion("126.0.6478.127").setup();
                ChromeOptions options = new ChromeOptions();
                options.addArguments("--lang=en-US");
                options.addArguments("--start-maximized");
                return new ChromeDriver(); }
            case "headless-chrome" -> {
                WebDriverManager.chromedriver().setup();
                ChromeOptions options = new ChromeOptions();
                options.addArguments("--headless");
                options.addArguments("--disable-gpu");
                options.addArguments("--window-size=1920,1080");
                return new ChromeDriver(options); }
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
                return new FirefoxDriver();
            }
            default -> throw new IllegalArgumentException("Invalid browser");
        }

    }

}
