package com.poc.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;

import java.util.Optional;


public class ExtentApiTestWatcher implements TestWatcher, BeforeTestExecutionCallback, AfterTestExecutionCallback {
    private static ExtentReports extentReports;
    private static ExtentTest test;


    public ExtentApiTestWatcher(){
    }

    public void setExtentTestReports( ExtentReports extentReports){
        ExtentApiTestWatcher.extentReports = extentReports;
    }

    public void setTest(ExtentTest extentTest) {
        test = extentTest;
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
