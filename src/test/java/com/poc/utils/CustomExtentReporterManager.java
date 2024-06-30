package com.poc.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.google.inject.Provides;
import com.google.inject.AbstractModule;

public class CustomExtentReporterManager extends AbstractModule {
    @Provides
    ExtentReports provideExtentReports() {
        ExtentSparkReporter sparkReporter = new ExtentSparkReporter("target/extent-report.html");
        ExtentReports extent = new ExtentReports();
        extent.attachReporter(sparkReporter);
        return extent;
    }
}
