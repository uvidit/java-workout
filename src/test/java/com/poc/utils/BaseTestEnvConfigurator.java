package com.poc.utils;

import com.aventstack.extentreports.ExtentReports;
import com.google.inject.Guice;
import com.google.inject.Injector;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

public class BaseTestEnvConfigurator {
    private static final Logger logger = Logger.getLogger(BaseTestEnvConfigurator.class.getName());
    private static BaseTestEnvConfigurator configurator;
    public static ExtentReports extent;
    public static final Properties props = new Properties();

    private BaseTestEnvConfigurator(){}

    public static BaseTestEnvConfigurator getConfigurator() throws IOException {
        if (configurator == null){
            configurator = new BaseTestEnvConfigurator();

            FileInputStream configFile = new FileInputStream(
                    String.format("src/test/resources/%s.properties"
//                        , System.getProperty("env", "local-chrome")));
                            , System.getProperty("env", "local-firefox")));
            props.load(configFile);


            Injector injector = Guice.createInjector( new CustomExtentReporterManager());
            extent = injector.getInstance(ExtentReports.class);
        }
        return configurator;
    }
}
