package com.poc.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;


public class ExtentUiTestWatcher implements TestWatcher, BeforeTestExecutionCallback, AfterTestExecutionCallback {
    private static ExtentReports extentReports;
    private static WebDriver driver;
    private static ExtentTest test;

    public ExtentUiTestWatcher() throws IOException {
    }

    public void setExtentTestReports( ExtentReports extentReports){
        ExtentUiTestWatcher.extentReports = extentReports;
    }

    public void setTest(ExtentTest extentTest) {
        test = extentTest;
    }

    public void setDriver(WebDriver webDriver) {
        driver = webDriver;
    }

    public ExtentTest getTest() {
        return test;
    }

    @Override
    public void testDisabled(ExtensionContext context, Optional<String> reason) {
        test = extentReports.createTest(context.getDisplayName());
        test.log(Status.SKIP, "Test Disabled\n" + reason.orElse("No reason given"));
    }

    @Override
    public void testSuccessful(ExtensionContext context) {
        test.log(Status.PASS, "Test Passed");
    }

    @Override
    public void testAborted(ExtensionContext context, Throwable cause) {
        test.log(Status.FAIL, "Test Aborted\n" + cause.getMessage());
    }

    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        test.log(Status.FAIL, "Test Failed\n" + cause);
        // Take screenshot if the test fails
        File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        try {
            Path screenshotPath = Paths.get("target/screenshots", context.getDisplayName() + ".png");
            Files.copy(screenshot.toPath(), screenshotPath);
            test.addScreenCaptureFromPath("screenshots/" + context.getDisplayName() + ".png");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void afterTestExecution(ExtensionContext extensionContext) {
        if (extentReports != null) {
            extentReports.flush();
        }
    }

    @Override
    public void beforeTestExecution(ExtensionContext extensionContext) {
        extensionContext.getTestInstances().ifPresent(testInstance -> {
            test.log(Status.INFO, " - You can log here as well (" + extensionContext.getDisplayName() + ").");
        });
    }
}
