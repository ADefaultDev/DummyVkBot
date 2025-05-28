package com.adefaultdev.DummyVkBot.browser;

import java.io.InputStream;
import java.util.Properties;

public class BrowserConfig {
    private static final String PROPERTIES_FILE = "config.properties";
    private static final String browserPath;
    private static final String driverVersion;

    static {
        try (InputStream input = BrowserConfig.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE)) {
            Properties prop = new Properties();
            prop.load(input);
            browserPath = prop.getProperty("browser.path");
            driverVersion = prop.getProperty("driver.version");
        } catch (Exception e) {
            throw new RuntimeException("Failed to load config file", e);
        }
    }

    public static String getBrowserPath() {
        return browserPath;
    }

    public static String getDriverVersion() {
        return driverVersion;
    }
}