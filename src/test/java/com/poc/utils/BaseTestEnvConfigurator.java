package com.poc.utils;

import com.aventstack.extentreports.ExtentReports;
import com.google.inject.Guice;
import com.google.inject.Injector;

import java.util.logging.Logger;

public class BaseTestEnvConfigurator {
    private static final Logger logger = Logger.getLogger(BaseTestEnvConfigurator.class.getName());
    private static BaseTestEnvConfigurator configurator;
    public static ExtentReports extent;
    private BaseTestEnvConfigurator(){}

    public static BaseTestEnvConfigurator getConfigurator(){
        if (configurator == null){
            configurator = new BaseTestEnvConfigurator();
            Injector injector = Guice.createInjector( new CustomExtentReporterManager());
            extent = injector.getInstance(ExtentReports.class);
        }
        return configurator;
    }
}
