package com.poc.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.google.inject.Provides;
import com.google.inject.AbstractModule;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CustomExtentReporterManager extends AbstractModule {
    @Provides
    ExtentReports provideExtentReports() {
        // Create screenshots directory if it doesn't exist
        Path screenshotsDir = Paths.get("target/screenshots");
        if (!Files.exists(screenshotsDir)) {
            try {
                Files.createDirectories(screenshotsDir);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        ExtentSparkReporter sparkReporter = new ExtentSparkReporter("extent-report.html");
        ExtentReports extent = new ExtentReports();
        extent.attachReporter(sparkReporter);
        return extent;
    }
}
